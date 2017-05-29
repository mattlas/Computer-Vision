package com.example.treemapp;

import android.app.Fragment;

import java.util.ArrayList;
import java.util.List;


import in.goodiebag.carouselpicker.CarouselPicker;



/**
 * Created by 5dv115 on 5/22/17.
 */

public class Settings extends Fragment {

    private String[] treesSpeciesAll;
    private List<CarouselPicker.PickerItem> treesSpeciesChosen;
    private int carouselSize = 12;
    private CheckboxModel[] treesSpeciesChb;

    public Settings(){

        // TODO put all possible species
        treesSpeciesAll = new String[]{"pine", "spruce", "birch", "oak", "aspen", "beech", "alder", "rowan", "elm", "ash", "hornbeam", "juniper", "cherry", "fir", "hazel", "horseChestnut", "lind", "larch", "maple", "oxel","sallow" };
        treesSpeciesChb = new CheckboxModel[treesSpeciesAll.length];
        treesSpeciesChosen = new ArrayList<>();
        int i = 0;
        for (String tree:treesSpeciesAll){
            treesSpeciesChosen.add(new CarouselPicker.TextItem(tree, carouselSize));
            if (i< 6)
                treesSpeciesChb[i] = new CheckboxModel(tree, 1);
            else
                treesSpeciesChb[i] = new CheckboxModel(tree, 0);
        }
    }

    public String[] getTreesSpeciesAll() {
        return treesSpeciesAll;
    }

    public List<CarouselPicker.PickerItem> getTreesSpeciesChosen() {
        return treesSpeciesChosen;
    }

      /**
     * Adds to the checked tree list chosen tree
     * @param tree the name of the tree to add to the list
     */
    public void addToChecked(String tree) {
        if (searchForObject(tree) == -1)
            treesSpeciesChosen.add(new CarouselPicker.TextItem(tree, carouselSize));
    }

    /**
     * Search for the objext in the list of specified name
     * @param tree - the name of the tree to be found in the list
     * @return position of the object, -1 if there is no object
     */
    private int searchForObject(String tree)
    {
        int pos = 0;
        Boolean found = false;
        for(CarouselPicker.PickerItem ti:treesSpeciesChosen){
            if (ti.getText().equals(tree)){
                found=true;
                break;
            }
            pos++;
        }
        if(found)
            return pos;
        else
            return -1;
    }

    /**
     * Removes the tree from a chosen species list
     * @param tree the name of the tree to be removed from the list
     */
    public void removeFromChecked(String tree) {
        int pos = searchForObject(tree);
        if ( pos != -1)
            treesSpeciesChosen.remove(pos);
    }

    /**
     * Returns a list for adapter with all trees
     * @return
     */
    public CheckboxModel[] getTreesSpeciesChb() {
        return treesSpeciesChb;
    }
}
