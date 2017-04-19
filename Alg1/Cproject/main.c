#include <vl/sift.h>
#include <vl/stringop.h>
#include <vl/pgm.h>
#include <vl/generic.h>
#include <vl/getopt_long.h>


#include <stdio.h>


/** @brief Keypoint ordering
** @internal
**/
int
korder (void const* a, void const* b) {
    double x = ((double*) a) [2] - ((double*) b) [2] ;
    if (x < 0) return -1 ;
    if (x > 0) return +1 ;
    return 0 ;
}


int main(int argc, char **argv) {
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

    /* PROCESS IMAGE -------------------------- */

    char basename [1024] ;
    char const *name = "/home/5dv115/c13evk_scripts/test/DSC01087_geotag.pgm";


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

    data  = malloc(vl_pgm_get_npixels (&pim) *
                   vl_pgm_get_bpp       (&pim) * sizeof (vl_uint8)   ) ;
    fdata = malloc(vl_pgm_get_npixels (&pim) *
                   vl_pgm_get_bpp       (&pim) * sizeof (vl_sift_pix)) ;

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
return exit_code ;
}

