/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicmodel;

import dmoz.util.Category;
import java.io.*;
import java.util.*;

/**
 *
 * @author hoshun
 */
public class TopicComposition {

    public static void main(String[] args) {

        System.out.println(whichMax(new double[]{1, 2, 3, 2, 6}));
    }

    public TopicComposition(File topicCompositionFile, int categoryLevel, int nOfTopic) {
        numberOfLDATopics = nOfTopic;
        docidToCategories = new ArrayList<Set<Category>>();
        docidToTopicVec = new ArrayList<double[]>();


        try {
            Scanner in = null;
            PrintWriter out = null;
            try {
                in = new Scanner(new BufferedReader(new FileReader(topicCompositionFile)));

                // read the description line
                in.nextLine();
                while (in.hasNextLine()) {
                    String[] tokens = in.nextLine().split("\\s");
                    int docid = Integer.parseInt(tokens[0]);
                    String docName = tokens[1];
                    String[] topicPropTokens = Arrays.copyOfRange(tokens, 2, tokens.length);
                    Map<Category, Integer> categories = ProfileDocuments.getCategory(docName);

                    Set<Category> catset = new TreeSet<Category>();
                    if (categoryLevel == 2) {
                        catset = categories.keySet();
                    } else if (categoryLevel == 1) {

                        for (Category cat : categories.keySet()) {
                            cat.second = "";
                            catset.add(cat);
                        }

                    }
                    docidToCategories.add(docid, catset);

                    double[] topicVec = formTopicVector(topicPropTokens);
                    docidToTopicVec.add(docid, topicVec);

                }
                ///
                constructSoftAndHardModel();


            } finally {
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void constructSoftModel() {
        categoryToTopicVectors = new TreeMap<Category, double[]>();
        for (int docid = 0; docid < docidToCategories.size(); docid++) {
            Set<Category> categories = docidToCategories.get(docid);
            int nOfCat = categories.size();
            double[] docTopicVec = docidToTopicVec.get(docid);

            docTopicVec = divide(docTopicVec, (double) nOfCat);

            for (Category cat : categories) {
                if (this.categoryToTopicVectors.containsKey(cat)) {
                    this.categoryToTopicVectors.put(cat,
                            addVectors(this.categoryToTopicVectors.get(cat), docTopicVec));
                } else {
                    this.categoryToTopicVectors.put(cat, docTopicVec);
                }
            }

        }

        for (double[] topicvec : categoryToTopicVectors.values()) {
            normalized(topicvec);

        }

        System.out.println("original");
        printSoftTopicModel();

    }

    private void printSoftTopicModel() {
        //Cateogry to topic vector
        for (Map.Entry<Category, double[]> en : categoryToTopicVectors.entrySet()) {
            System.out.printf(en.getKey().first + ":" + en.getKey().second);
            double[] vec = en.getValue();
            for (int i = 0; i < vec.length; i++) {
                System.out.printf(" %d:%2.3f", i, vec[i]);
            }
            System.out.printf("\n");
        }
    }

    public void constructNormalizeSoftModel() {
        double[] sumvec = sumVectors(this.categoryToTopicVectors.values());
        for (double[] vec : this.categoryToTopicVectors.values()) {
            vec = elementwiseDivide(vec, sumvec);
        }
        System.out.println("normalized");
        printSoftTopicModel();
    }

    private static double[] sumVectors(Collection<double[]> vecset) {
        if (vecset.isEmpty()) {
            System.out.println("NO vectors to sum");
            return null;
        }
        double[] vecsum = new double[vecset.iterator().next().length];
        for (double[] vec : vecset) {
            vecsum = addVectors(vecsum, vec);
        }
        return vecsum;
    }

    private static double[] elementwiseDivide(double[] p, double[] q) {
        double[] r = new double[p.length];
        for (int i = 0; i < p.length; i++) {
            r[i] = p[i] / q[i];
        }
        return r;
    }

    //@TODO let the highest (normalized) topic choose category first, ensure 1-1 mapping. 
    public void MakeTopicToCategory() {
        double[] sumvec = sumVectors(this.categoryToTopicVectors.values());

    }

    private void constructSoftAndHardModel() {
        constructSoftModel();
        categoryToLDATopic = new TreeMap<Category, Integer>();

        for (Map.Entry<Category, double[]> en : categoryToTopicVectors.entrySet()) {
            Category cat = en.getKey();
            int maxid = whichMax(en.getValue());
            categoryToLDATopic.put(cat, maxid);
        }

        printHardTopicModel();

    }

    private void printHardTopicModel() {

        for (Map.Entry<Category, Integer> en : categoryToLDATopic.entrySet()) {
            System.out.println(en.getKey() + " LDA topic: " + en.getValue());
        }
    }

    public void evalSoftModelWithKLDivergence() {
        double[] rs = new double[docidToTopicVec.size()];
        for (int docid = 0; docid < docidToTopicVec.size(); docid++) {
            Set<Category> catset = docidToCategories.get(docid);

            double[] avg = new double[numberOfLDATopics];
            for (Category cat : catset) {
                avg = addVectors(avg, categoryToTopicVectors.get(cat));
            }
            avg = divide(avg, catset.size());

            rs[docid] = KLDivergence(docidToTopicVec.get(docid), avg);

        }
        System.out.println("Soft eval: ");
        System.out.println("KLDivergence mean: " + mean(rs));
        System.out.println("KLDivergence sd: " + sd(rs));
    }

    public void evalHardModel() {
        // int [][] confMatrix = new int [this.numberOfLDATopics][this.numberOfLDATopics];
        int correct = 0;
        for (int docid = 0; docid < docidToCategories.size(); docid++) {
            Set<Category> catset = docidToCategories.get(docid);
            int label = whichMax(docidToTopicVec.get(docid));
            boolean b = false;
            for (Category cat : catset) {
                if (label == categoryToLDATopic.get(cat)) {
                    b = true;
                    correct++;
                    break;
                }
            }
        }
        int nOfDoc = docidToCategories.size();
        System.out.println("Hard eval: ");
        System.out.printf("%d correct from %d documents. rate %2.3f\n", correct, nOfDoc, correct / (double) nOfDoc);

    }

    // normalize the docid-topicvector 
    @Deprecated
    public void evalNormalizedHardModel() {
        // int [][] confMatrix = new int [this.numberOfLDATopics][this.numberOfLDATopics];
        int correct = 0;
        double[] sumvec = sumVectors(this.categoryToTopicVectors.values());
        for (int docid = 0; docid < docidToCategories.size(); docid++) {
            Set<Category> catset = docidToCategories.get(docid);

            double[] normalizedvec = elementwiseDivide(docidToTopicVec.get(docid), sumvec);
            int label = whichMax(normalizedvec);

            boolean b = false;
            for (Category cat : catset) {
                if (label == categoryToLDATopic.get(cat)) {
                    b = true;
                    correct++;
                    break;
                }
            }
        }
        int nOfDoc = docidToCategories.size();
        System.out.println("Normalized Hard eval: ");
        System.out.printf("%d correct from %d documents. rate %2.3f\n", correct, nOfDoc, correct / (double) nOfDoc);
    }

    public void evalHardModelinPercentage() {
        // int [][] confMatrix = new int [this.numberOfLDATopics][this.numberOfLDATopics];
        double correctPercent = 0;
        for (int docid = 0; docid < docidToCategories.size(); docid++) {
            Set<Category> catset = docidToCategories.get(docid);

            double [] topicvec = docidToTopicVec.get(docid);
            boolean b = false;
            for (Category cat : catset) {
                int topicId = categoryToLDATopic.get(cat);
                correctPercent += topicvec[topicId];
            }
        }
        int nOfDoc = docidToCategories.size();
        System.out.println("Hard Percentage eval: ");
        System.out.printf("%2.3f correct from %d documents. rate %2.3f\n", correctPercent, nOfDoc, correctPercent / (double) nOfDoc);

    }

    /*
     * Using modelVec to describe instanceVec.
     */
    private static double KLDivergence(double[] instanceVec, double[] modelVec) {
        if (instanceVec.length != modelVec.length || instanceVec.length == 0) {
            System.out.println("Invalid KL divergence input");
        }

        double rs = 0;
        for (int i = 0; i < instanceVec.length; i++) {
            rs += instanceVec[i] * Math.log(instanceVec[i] / modelVec[i]);
        }
        return rs;
    }

    private double[] formTopicVector(String[] topicPropTokens) {
        double[] p = new double[this.numberOfLDATopics];
        for (int i = 0; i < topicPropTokens.length; i = i + 2) {
            int topicid = Integer.parseInt(topicPropTokens[i]);
            double val = Double.parseDouble(topicPropTokens[i + 1]);
            p[topicid] = val;
        }
        return p;
    }

    private static void normalized(double[] p) {
        double sum = 0;
        for (double v : p) {
            sum += v;
        }
        for (int i = 0; i < p.length; i++) {
            p[i] = p[i] / sum;
        }
    }

    private static double[] addVectors(double[] p, double[] q) {
        double[] r = new double[p.length];
        if (p.length != q.length) {
            System.out.println("vector not of equal length");
        }
        for (int i = 0; i < p.length; i++) {
            r[i] = p[i] + q[i];
        }
        return r;
    }

    private static double[] divide(double[] p, double divider) {
        double[] r = new double[p.length];
        for (int i = 0; i < p.length; i++) {
            r[i] = p[i] / divider;
        }
        return r;
    }

    public static double mean(double[] p) {
        double sum = 0;  // sum of all the elements
        for (int i = 0; i < p.length; i++) {
            sum += p[i];
        }
        return sum / p.length;
    }

    public static double sd(double[] p) {
        double m = mean(p);
        double sqsum = 0;
        for (double v : p) {
            sqsum += Math.pow((v - m), 2);
        }
        return Math.sqrt(sqsum / p.length);
    }

    private static int whichMax(double[] p) {
        double max = -1;
        int id = -1;
        for (int i = 0; i < p.length; i++) {
            if (p[i] > max) {
                id = i;
                max = p[i];
            }
        }
        return id;
    }

    private void printVector(double[] p) {
        for (double v : p) {
            System.out.printf("%3.2f ", v);
        }
        System.out.printf("\n");
    }

    public void setCategoryToLDATopic(int[] label) {
        int iter = 0;
        for (Category cat : categoryToLDATopic.keySet()) {
            categoryToLDATopic.put(cat, label[iter]);
            iter++;
        }
        System.out.println("Set result: ");
        printHardTopicModel();
    }
    ArrayList<Set<Category>> docidToCategories;
    ArrayList<double[]> docidToTopicVec;
    // model parameter 
    Map<Category, double[]> categoryToTopicVectors;
    Map<Category, Integer> categoryToLDATopic;// for hard clustering 
    private int numberOfLDATopics;
}
