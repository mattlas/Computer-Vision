//============================================================================
// Name        : TreeMarkupToolbox.cpp
// Author      : Machine Vision UMU
// Version     : 1.0
// Copyright   : GNU
// Description : Method for positioning field measured trees relative to image data for building a 3D model.
//============================================================================

#include <iostream>
#include "FeaturePoints.h"
#include "MosaicData.h"


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


    const char *path;
    if(argc > 1){
    	path = argv[1];
    } else {
    	path = std::string("").data();
    }

    MosaicData *data = new MosaicData();
    data->addDirectory("/home/5dv115/c13evk_scripts/output");
    data->startProcess();
    return app.exec();
}
