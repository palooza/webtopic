/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dmoz.topicmodel;

import java.util.*;
import java.io.*;
import dmoz.util.*;

/**
 *
 * @author hoshun
 */
public class ExperimentSimilarity {

    public static void main(String[] args) {

        File vecfile = new File("/home/hoshun/dmoz/dmoz-doc-normalized-tfidf");
        File cat2file = new File("/home/hoshun/dmoz/dmoz-category2-normalized-tfidf");
        File cat1file = new File("/home/hoshun/dmoz/dmoz-category1-normalized-tfidf");

        Map<Category, NormalizedWordVector> cat1VecMap =
                NormalizedWordVector.readNormalizedWordVectors(cat1file);

        Map<Category, NormalizedWordVector> cat2VecMap =
                NormalizedWordVector.readNormalizedWordVectors(cat2file);


        expWithCategory1(vecfile, cat1VecMap);
        //expWithCategory2(vecfile, cat2VecMap);
        //expCat2WithCat1(cat1VecMap, cat2VecMap);
    }

    public static void expCat2WithCat1(Map<Category, NormalizedWordVector> cat1VecMap,
            Map<Category, NormalizedWordVector> cat2VecMap) {

        double correct = 0;
        double nLine = 0;
        ConfusionMatrix confMatrix = new ConfusionMatrix(cat1VecMap.keySet());
        for (Map.Entry<Category, NormalizedWordVector> en : cat2VecMap.entrySet()) {
            nLine++;
            Category cat = en.getKey();
            cat.second = "";
            Record[] rs = topNSimilarCategory(1, en.getValue(), cat1VecMap);
            confMatrix.add(cat, rs[0].category);
            if (cat.equals(rs[0].category)) {
                correct++;
            }

            if ((nLine % 100000) == 0) {
                System.out.println("current line: " + nLine);
            }
        }

        confMatrix.print(new File("/home/hoshun/dmoz/cat1cat2-confmatrix"));
        System.out.printf("%s of %s doc is correctly categorized (in top1 level). \n", correct, nLine);
        System.out.printf("The ratio is %s\n", (correct / nLine * 100.0));
    }

    public static void expWithCategory1(File vecfile,
            Map<Category, NormalizedWordVector> cat1VecMap) {

        ConfusionMatrix confMatrix = new ConfusionMatrix(cat1VecMap.keySet());

        System.out.println("start evaluating cat1...");
        double correct = 0;
        double nLine = 0;
        try {
            Scanner in = new Scanner(new BufferedReader(new FileReader(vecfile)));
            int n = 1;

            while (in.hasNextLine()) {
                nLine++;
                String[] tokens = in.nextLine().split("\t");
                Category cat = new Category(tokens[0], "");
                NormalizedWordVector nwv = new NormalizedWordVector(Arrays.copyOfRange(tokens, 2, tokens.length));
                Record[] rs = topNSimilarCategory(n, nwv, cat1VecMap);

                confMatrix.add(cat, rs[0].category);
                if (rs[0].category.equals(cat)) {
                    correct++;
                }

                if ((nLine % 100000) == 0) {
                    System.out.println("current line: " + nLine);
                }
            }
            confMatrix.print(new File("/home/hoshun/dmoz/cat1-confmatrix"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.printf("%s of %s doc is correctly categorized (in top1 level). \n", correct, nLine);
        System.out.printf("The ratio is %s\n", (correct / nLine * 100.0));
    }

    public static void expWithCategory2(File vecfile,
            Map<Category, NormalizedWordVector> cat2VecMap) {

        System.out.println("start evaluating cat2...");

        int n = 3;
        double firstcorrect = 0;
        double roughcorrect = 0;
        double nLine = 0;
        try {
            Scanner in = new Scanner(new BufferedReader(new FileReader(vecfile)));
            while (in.hasNextLine()) {
                nLine++;
                String[] tokens = in.nextLine().split("\t");
                Category cat = new Category(tokens[0], tokens[1]);
                NormalizedWordVector nwv = new NormalizedWordVector(Arrays.copyOfRange(tokens, 2, tokens.length));
                Record[] rs = topNSimilarCategory(n, nwv, cat2VecMap);
                if (rs[0].category.equals(cat)) {
                    firstcorrect++;
                    roughcorrect++;
                } else {
                    if (rs[1].category.equals(cat) || rs[2].category.equals(cat)) {
                        roughcorrect++;
                    }
                }

                if ((nLine % 100000) == 0) {
                    System.out.println("current line: " + nLine);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.printf("%s of %s doc is correctly categorized. \n", firstcorrect, nLine);
        System.out.printf("The ratio is %s\n", (firstcorrect / nLine * 100.0));

        System.out.printf("%s of %s doc is correctly categorized by the top3. \n", roughcorrect, nLine);
        System.out.printf("The ratio is %s\n", (roughcorrect / nLine * 100.0));
    }

    /**
     * Return Record(Category, similarity value) in descending order.
     *
     * @param n
     * @param nwv
     * @param categoryToWordVector
     * @return
     */
    public static Record[] topNSimilarCategory(int n, NormalizedWordVector nwv,
            Map<Category, NormalizedWordVector> categoryToWordVector) {
        // PriorityQueue by default is a min heap.
        PriorityQueue<Record> heap = new PriorityQueue<Record>();

        for (Map.Entry<Category, NormalizedWordVector> en : categoryToWordVector.entrySet()) {
            double simVal = nwv.innerProduct(en.getValue());
            Category cat = en.getKey();
            if (heap.size() < n) {
                heap.add(new Record(cat, simVal));
            } else {
                if (heap.peek().value < simVal) {
                    heap.remove();
                    heap.add(new Record(cat, simVal));
                }
            }
        }

        Record[] result = new Record[n];
        for (int i = (n - 1); i >= 0; i--) {
            result[i] = heap.poll();
        }

        return result;
    }
}
