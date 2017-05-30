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
#include "DirectoryReader.h"
//#include "imaq.h"
#include <string>


// Qt
#include "mainwindow.h"
#include <QApplication>
#include <QMainWindow>

extern "C"{
#include <vl/generic.h>
}

int main(int argc, char **argv){
    //VL_PRINT ("vlfeat loaded properly\n");
    //This can be used instead of gui to run a certain folder
    /*
    MosaicData *data = new MosaicData("/home/5dv115/c13evk_scripts/pict","/home/5dv115/c13evk_scripts/pict_out" );
    data->startProcess();
    std::cout << "time = " << time << std::endl;
    */
    // Qt
   QApplication app(argc, argv);

    MainWindow w;
    w.show();

    return app.exec();
}
