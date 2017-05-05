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

// Qt
#include <QtGui>
#include <QApplication>
#include <QPushButton>

extern "C"{
#include <vl/generic.h>
}

int main(int argc, char **argv){
    VL_PRINT ("vlfeat loaded properly\n");

    // Qt
    QApplication app(argc, argv);
    QPushButton *button = new QPushButton("Hello World");
    button->show();

    FeaturePoints *points = new FeaturePoints();
    points->testClass();
    points->calculatePoints("filnamn");

    return app.exec();
}
