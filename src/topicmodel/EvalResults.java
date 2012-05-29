/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicmodel;

import java.util.*;
import java.io.*;

/**
 *
 * @author hoshun
 */
public class EvalResults {

    public static void main(String[] args) {
        String directory = "/home/hoshun/webtopic/topicmodel/";
        int nTopic = 8;
        String type = "basic-specify";

        File topicCompositionInfile = new File(String.format("%sbasic/%s-topic%d-composition.out", directory, type, nTopic));
        File topicKeysInfile = new File(String.format("%sbasic/%s-topic%d-composition.out", directory, type, nTopic));

        
        TopicComposition tc = new TopicComposition(topicCompositionInfile, 1, nTopic);
     
        tc.evalSoftModelWithKLDivergence();
        tc.evalHardModel();
        
        tc.evalHardModelinPercentage();
        
//        tc.constructNormalizeSoftModel();
//        tc.setCategoryToLDATopic(new int [] {1,0,1,1});
//        tc.evalNormalizedHardModel();
        
    }

    public static void EvalResutls() {
    }


}
