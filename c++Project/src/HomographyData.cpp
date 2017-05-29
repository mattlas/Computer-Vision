//
// Created by 5dv115 on 5/26/17.
//

#include "HomographyData.h"

HomographyData::HomographyData() {

}

const HomographyData &HomographyData::getPrevNode() const {
    return prevNode;
}

void HomographyData::setPrevNode(const HomographyData &prevNode) {
    HomographyData::prevNode = prevNode;
}

int HomographyData::getRecursionDepth() const {
    return recursionDepth;
}

void HomographyData::setRecursionDepth(int recursionDepth) {
    HomographyData::recursionDepth = recursionDepth;
}
