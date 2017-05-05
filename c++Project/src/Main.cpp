//============================================================================
// Name        : TreeMarkupToolbox.cpp
// Author      : Machine Vision UMU
// Version     : 1.0
// Copyright   : GNU
// Description : Method for positioning field measured trees relative to image data for building a 3D model.
//============================================================================

#include <iostream>
#include "FeaturePoints.h"
#include "KeyPoint.h"

extern "C"{
#include <vl/generic.h>
}

int main(){
    VL_PRINT ("vlfeat loaded properly\n");
<<<<<<< HEAD

    // Qt
    QApplication app(argc, argv);
    QPushButton *button = new QPushButton("Hello World");
    button->show();

    const char *path;
    if(argc > 1){
    	path = argv[1];
    } else {
    	path = std::string("").data();
    }
    FeaturePoints *points = new FeaturePoints();
    points->testClass();
    points->calculatePoints(path);

    return app.exec();
=======
    FeaturePoints *points = new FeaturePoints();
    points->testClass();
    points->calculatePoints("filnamn");
    return 0;
>>>>>>> c63c811949615e290290249c405c5b28c4623e75
}
