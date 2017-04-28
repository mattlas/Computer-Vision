//
// Created by 5dv115 on 4/28/17.
//

#include "KeyPoint.h"

KeyPoint::KeyPoint(void) {

}

KeyPoint::KeyPoint(float x, float y, float scale, float orientation) {
    this->x = x;
    this->y = y;
    this->scale = scale;
    this->orientation = orientation;
}


float KeyPoint::getX() const {
    return x;
}

void KeyPoint::setX(float x) {
    KeyPoint::x = x;
}

float KeyPoint::getY() const {
    return y;
}

void KeyPoint::setY(float y) {
    KeyPoint::y = y;
}

float KeyPoint::getScale() const {
    return scale;
}

void KeyPoint::setScale(float scale) {
    KeyPoint::scale = scale;
}

float KeyPoint::getOrientation() const {
    return orientation;
}

void KeyPoint::setOrientation(float orientation) {
    KeyPoint::orientation = orientation;
}