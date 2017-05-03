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
#include <QtGui>
#include <QApplication>
#include <QPushButton>

extern "C"{
#include <vl/generic.h>
}

int main(int argc, char **argv){
    VL_PRINT ("vlfeat loaded properly\n");
    //FeaturePoints *points = new FeaturePoints();
    //points->testClass();
    //points->calculatePoints("filnamn");

    //app is an object that governs the application
    //make sure everything is initialized correctly
    QApplication app(argc, argv);

    //QPushButton is a widget
    //the text of the button is Hello World
    QPushButton *button = new QPushButton("Hello World");
    //show the button
    button->show();

    return app.exec();
}
