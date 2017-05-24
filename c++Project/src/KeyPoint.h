/**
 * Class to store information about a keyPoint. Keypoint stores the points coordinates in x,y. The scale of the
 * Keypoint and the orientation (Orientation might not be accurate)
 */

#ifndef TREEMARKUPTOOLBOX_KEYPOINT_H
#define TREEMARKUPTOOLBOX_KEYPOINT_H

class KeyPoint {
private:
    float x;
    float y;
    float scale;
    float orientation;
public:
    KeyPoint(void);
    KeyPoint(float, float,float, float);

    float getX() const;

    void setX(float x);

    float getY() const;

    void setY(float y);

    float getScale() const;

    void setScale(float scale);

    float getOrientation() const;

    void setOrientation(float orientation);


};
#endif //TREEMARKUPTOOLBOX_KEYPOINT_H
