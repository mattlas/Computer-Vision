The makefile compiles test.c which links to VLfeats print function.
the makefile -I and -L needs to be the path of vlfeats libraries.
/vlfeat-0.9.20/bin/glnxa64 also needs to be added to ldconfig. see https://codeyarns.com/2014/01/14/how-to-add-library-directory-to-ldconfig-cache/ for guide.
