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

//Qt
#include <qapplication.h>
#include <qpushbutton.h>

extern "C"{
#include <vl/generic.h>
}

int main(int argc, char **argv){
    VL_PRINT ("vlfeat loaded properly\n");
    //FeaturePoints *points = new FeaturePoints();
    //points->testClass();
    //points->calculatePoints("filnamn");

    QApplication a(argc, argv);

    QPushButton hello( "Hello world!", 0 );
    hello.resize( 100, 30 );

    hello.show();

    return 0;
}
