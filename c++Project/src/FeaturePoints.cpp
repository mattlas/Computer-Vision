//
// Created by 5dv115 on 4/26/17.
//
#include "FeaturePoints.h"
#include <iostream>
#include <fstream>
#include <string.h>


extern "C" {
#include <vl/sift.h>
#include <vl/stringop.h>
#include <vl/pgm.h>
#include <vl/generic.h>
#include <vl/getopt_long.h>
#include "src/generic-driver.h"
}

/*int main( int argc, char** argv ) {
    VL_PRINT("vlfeat loaded properly\n");
    FeaturePoints *points = new FeaturePoints();
    points->testClass();
    points->calculatePoints("filnamn");
    return 0;
}*/


FeaturePoints::FeaturePoints(void) {

}

void FeaturePoints::testClass() {
    std::cout << "FeaturePoint test function" << std::endl;
}



int korder (void const* a, void const* b) {
    double x = ((double*) a) [2] - ((double*) b) [2] ;
    if (x < 0) return -1 ;
    if (x > 0) return +1 ;
    return 0 ;
}


void FeaturePoints::calculatePoints(char const *path) {

    /* algorithm parameters */
    double   edge_thresh  = -1 ;
    double   peak_thresh  = -1 ;
    double   magnif       = -1 ;
    int      O = -1, S = 3, omin = -1 ;

    vl_bool  err    = VL_ERR_OK ;
    char     err_msg [1024] ;
    int      n ;
    int      exit_code          = 0 ;
    int      verbose            = 0 ;
    vl_bool  force_output       = 0 ;
    vl_bool  force_orientations = 0 ;


    VlFileMeta dsc  = {1, "%.descr", VL_PROT_ASCII, "", 0} ;
    VlFileMeta ifr  = {0, "%.frame", VL_PROT_ASCII, "", 0} ;
    /* PROCESS IMAGE -------------------------- */

    char basename [1024] ;

    //char const *name = path;


    char const *name;
    if(strcmp(path, "")) {
    	name = path;
    } else {
    	name = "/home/5dv115/c13evk_scripts/output/DSC01104_geotag.pgm";
    }

    FILE            *in    = 0 ;
    vl_uint8        *data  = 0 ;
    vl_sift_pix     *fdata = 0 ;
    VlPgmImage       pim ;

    VlSiftFilt      *filt = 0 ;
    vl_size          q ;
    int              i ;
    vl_bool          first ;

    double           *ikeys = 0 ;
    int              nikeys = 0, ikeys_size = 0 ;
    int nKeypoints = 0;



    /* get basenmae from filename */
    q = vl_string_basename (basename, sizeof(basename), name, 1) ;

    err = (q >= sizeof(basename)) ;

    if (err) {
        snprintf(err_msg, sizeof(err_msg),
                 "Basename of '%s' is too long", name);
        err = VL_ERR_OVERFLOW ;
        goto done ;
    }

    /* open input file */
    in = fopen (name, "rb") ;
    if (!in) {
        err = VL_ERR_IO ;
        snprintf(err_msg, sizeof(err_msg),
                 "Could not open '%s' for reading.", name) ;
        goto done ;
    }

    /* read PGM header */
    err = vl_pgm_extract_head (in, &pim) ;

    if (err) {
        switch (vl_get_last_error()) {
            case  VL_ERR_PGM_IO :
                snprintf(err_msg, sizeof(err_msg),
                         "Cannot read from '%s'.", name) ;
                err = VL_ERR_IO ;
                break ;

            case VL_ERR_PGM_INV_HEAD :
                snprintf(err_msg, sizeof(err_msg),
                         "'%s' contains a malformed PGM header.", name) ;
                err = VL_ERR_IO ;
                goto done ;
        }
    }

    data  = (vl_uint8 *) malloc(vl_pgm_get_npixels (&pim) *
                                vl_pgm_get_bpp       (&pim) * sizeof (vl_uint8)   );
    fdata = (vl_sift_pix *) malloc(vl_pgm_get_npixels (&pim) *
                                   vl_pgm_get_bpp       (&pim) * sizeof (vl_sift_pix));

    if (!data || !fdata) {
        err = VL_ERR_ALLOC ;
        snprintf(err_msg, sizeof(err_msg),
                 "Could not allocate enough memory.") ;
        goto done ;
    }

    /* read PGM body */
    err  = vl_pgm_extract_data (in, &pim, data) ;

    if (err) {
        snprintf(err_msg, sizeof(err_msg), "PGM body malformed.") ;
        err = VL_ERR_IO ;
        goto done ;
    }

    /* convert data type */
    for (q = 0 ; q < (unsigned) (pim.width * pim.height) ; ++q) {
        fdata [q] = data [q] ;
    }

#define WERR(name,op)                                           \
    if (err == VL_ERR_OVERFLOW) {                               \
      snprintf(err_msg, sizeof(err_msg),                        \
               "Output file name too long.") ;                  \
      goto done ;                                               \
    } else if (err) {                                           \
      snprintf(err_msg, sizeof(err_msg),                        \
               "Could not open '%s' for " #op, name) ;          \
      goto done ;                                               \
    }

    if (ifr.active) {

        /* open file */
        err = vl_file_meta_open (&ifr, basename, "rb") ;
        WERR(ifr.name, reading) ;

#define QERR                                                            \
      if (err ) {                                                       \
        snprintf (err_msg, sizeof(err_msg),                             \
                  "'%s' malformed", ifr.name) ;                         \
        err = VL_ERR_IO ;                                               \
        goto done ;                                                     \
      }

        while (1) {
            double x, y, s, th ;

            /* read next guy */
            err = vl_file_meta_get_double (&ifr, &x) ;
            if   (err == VL_ERR_EOF) break;
            else QERR ;
            err = vl_file_meta_get_double (&ifr, &y ) ; QERR ;
            err = vl_file_meta_get_double (&ifr, &s ) ; QERR ;
            err = vl_file_meta_get_double (&ifr, &th) ;
            if   (err == VL_ERR_EOF) break;
            else QERR ;

            /* make enough space */
            if (ikeys_size < nikeys + 1) {
                ikeys_size += 10000 ;
                ikeys       = (double *) realloc (ikeys, 4 * sizeof(double) * ikeys_size);
            }

            /* add the guy to the buffer */
            ikeys [4 * nikeys + 0]  = x ;
            ikeys [4 * nikeys + 1]  = y ;
            ikeys [4 * nikeys + 2]  = s ;
            ikeys [4 * nikeys + 3]  = th ;

            ++ nikeys ;
        }

        /* now order by scale */
        qsort (ikeys, nikeys, 4 * sizeof(double), korder) ;

        /* close file */
        vl_file_meta_close (&ifr) ;
    }

    err = vl_file_meta_open (&dsc, basename, "wb") ; WERR(dsc.name, writing) ;


    filt = vl_sift_new((int)pim.width, (int)pim.height, -1,5,0);

    i     = 0 ;
    first = 1 ;
    while (1) {
        VlSiftKeypoint const *keys = 0 ;
        int                   nkeys ;

        /* calculate the GSS for the next octave .................... */
        if (first) {
            first = 0 ;
            err = vl_sift_process_first_octave (filt, fdata) ;
        } else {
            err = vl_sift_process_next_octave  (filt) ;
        }

        if (err) {
            err = VL_ERR_OK ;
            break ;
        }

        /* run detector ............................................. */
        if (ikeys == 0) {
            vl_sift_detect (filt) ;

            keys  = vl_sift_get_keypoints     (filt) ;
            nkeys = vl_sift_get_nkeypoints (filt) ;
            i     = 0 ;
        } else {
            nkeys = nikeys ;
        }

        /* for each keypoint ........................................ */

        for (; i < nkeys ; ++i) {
            nKeypoints++;
            double                angles [4] ;
            int                   nangles ;
            VlSiftKeypoint        ik ;
            VlSiftKeypoint const *k ;


            /* obtain keypoint orientations ........................... */
            if (ikeys) {
                vl_sift_keypoint_init (filt, &ik,
                                       ikeys [4 * i + 0],
                                       ikeys [4 * i + 1],
                                       ikeys [4 * i + 2]) ;

                if (ik.o != vl_sift_get_octave_index (filt)) {
                    break ;
                }

                k          = &ik ;

                /* optionally compute orientations too */
                if (force_orientations) {
                    nangles = vl_sift_calc_keypoint_orientations
                            (filt, angles, k) ;
                } else {
                    angles [0] = ikeys [4 * i + 3] ;
                    nangles    = 1 ;
                }
            } else {
                k = keys + i ;
                nangles = vl_sift_calc_keypoint_orientations
                        (filt, angles, k) ;
            }

            /* for each orientation ................................... */
            for (q = 0 ; q < (unsigned) nangles ; ++q) {
                vl_sift_pix descr [128] ;

                /* Descriptors */
                vl_sift_calc_keypoint_descriptor
                        (filt, descr, k, angles [q]) ;
                KeyPoint *keyPoint = new KeyPoint(k->x, k->y, k->sigma, k->s);
                int l ;

                std::vector<uint8_t> descriptor;
                for (l = 0 ; l < 128 ; ++l) {
                    double x = 512.0 * descr[l] ;
                    x = (x < 255.0) ? x : 255.0 ;
                    descriptor.push_back((uint8_t)x);
                    vl_file_meta_put_uint8 (&dsc, (vl_uint8) (x)) ;

                }
                keyPoints.push_back(*keyPoint);
                descriptors.push_back(descriptor);
                fprintf(dsc.file, "\n") ;
            }
        }
    }
    std::cout << "kepoints= " << nKeypoints << std::endl;
    writeKeyPoints();

    done :
    /* release input keys buffer */
    if (ikeys) {
        free (ikeys) ;
        ikeys_size = nikeys = 0 ;
        ikeys = 0 ;
    }

    /* release filter */
    if (filt) {
        vl_sift_delete (filt) ;
        filt = 0 ;
    }

    /* release image data */
    if (fdata) {
        free (fdata) ;
        fdata = 0 ;
    }

    /* release image data */
    if (data) {
        free (data) ;
        data = 0 ;
    }

    /* close files */
    if (in) {
        fclose (in) ;
        in = 0 ;
    }

    vl_file_meta_close (&dsc) ;
    vl_file_meta_close (&ifr) ;

    /*vl_file_meta_close (&out) ;
    vl_file_meta_close (&frm) ;
    vl_file_meta_close (&dsc) ;
    vl_file_meta_close (&met) ;
    vl_file_meta_close (&gss) ;
    vl_file_meta_close (&ifr) ;*/

    /* if bad print error message */
    if (err) {
        fprintf
                (stderr,
                 "sift: err: %s (%d)\n",
                 err_msg,
                 err) ;
        exit_code = 1 ;
    }


/* quit */
}

void FeaturePoints::writeKeyPoints() {
    std::ofstream out_file;
    out_file.open("keypoints.txt");
    for(ulong i=0; i < keyPoints.size(); i++){
        KeyPoint point = keyPoints.at(i);
        out_file << point.getX() << "   ";
        out_file << point.getY() << "   ";
        out_file << point.getScale() << "   ";
        out_file << point.getOrientation() << "   " << std::endl;
    }
}

