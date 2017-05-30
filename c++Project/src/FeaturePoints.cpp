//
// Created by 5dv115 on 4/26/17.
//
#include "FeaturePoints.h"

FeaturePoints::FeaturePoints(std::string imageName, int id) {
    this->imageName = imageName;
}

int korder (void const* a, void const* b) {
    double x = ((double*) a) [2] - ((double*) b) [2] ;
    if (x < 0) return -1 ;
    if (x > 0) return +1 ;
    return 0 ;
}

void FeaturePoints::calculatePoints() {

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


    char const *name = imageName.c_str();



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




    filt = vl_sift_new((int)pim.width, (int)pim.height, -1,LEVELS,FIRST_OCTAVE);

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



            k = keys + i ;
            nangles = vl_sift_calc_keypoint_orientations
                    (filt, angles, k) ;


            /* for each orientation ................................... */
            for (q = 0 ; q < (unsigned) nangles ; ++q) {
                vl_sift_pix descr [128] ;

                /* Descriptors */
                vl_sift_calc_keypoint_descriptor
                        (filt, descr, k, angles [q]) ;
                KeyPoint *keyPoint = new KeyPoint(k->x, k->y, k->sigma, k->s);
                int l ;


                std::vector<uint16_t> descriptor;

                for (l = 0 ; l < 128 ; ++l) {
                    double x = 512.0 * descr[l] ;
                    x = (x < 255.0) ? x : 255.0 ;
                    descriptor.push_back((uint16_t)x);
                }
                keyPoints.push_back(*keyPoint);
                descriptors.push_back(descriptor);

            }
        }
    }

    done :


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

void FeaturePoints::writeKeyPoints(char* name) {
    std::ofstream out_file;
    std::string file_name = "outputfiles/";
    file_name.append(name);
    file_name.append("_keypoint.txt");
    out_file.open(file_name);

    for(ulong i=0; i < keyPoints.size(); i++){
        KeyPoint point = keyPoints.at(i);
        out_file << point.getX() << "   ";
        out_file << point.getY() << "   ";
        out_file << point.getScale() << "   ";
        out_file << point.getOrientation() << "   " << std::endl;
    }
    out_file.close();
}

void FeaturePoints::writeDescriptors(char* name) {
    std::ofstream out_file;
    std::string file_name = "outputfiles/";
    file_name.append(name);
    file_name.append("_descriptor.txt");
    out_file.open(file_name);
    for(ulong i=0; i < descriptors.size(); i++){
        std::vector<uint16_t> desc = (std::vector<uint16_t> &&) descriptors.at(i);
        for(ulong j=0; j < desc.size() ; j++ ){
            //fprintf(out_file, "%d    ",desc.at(j));
            out_file << desc.at(j) << "    ";
        }
        out_file << std::endl;
    }
    out_file.close();
}

const std::vector<KeyPoint> &FeaturePoints::getKeyPoints() const {
    return keyPoints;
}

void FeaturePoints::setKeyPoints(const std::vector<KeyPoint> &keyPoints) {
    FeaturePoints::keyPoints = keyPoints;
}

const std::vector<std::vector<uint16_t>> &FeaturePoints::getDescriptors() const {
    return descriptors;
}

void FeaturePoints::setDescriptors(const std::vector<std::vector<uint16_t>> &descriptors) {
    FeaturePoints::descriptors = descriptors;
}





