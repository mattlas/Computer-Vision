//============================================================================
// Name        : TreeMarkupToolbox.cpp
// Author      : Machine Vision UMU
// Version     : 1.0
// Copyright   : GNU
// Description : Method for positioning field measured trees relative to image data for building a 3D model.
//============================================================================

#include <iostream>
#include "FeaturePoints.h"

extern "C"{
#include <vl/generic.h>
}

int main(){
    VL_PRINT ("vlfeat loaded properly\n");
    FeaturePoints* points = new FeaturePoints();
    points->testClass();
    points->calculatePoints("filnamn");
    return 0;
}
