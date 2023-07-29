package edu.caltech.cs2.lab04;

import java.util.ArrayList;
import java.util.List;

public class DecisionTree {
    private final DecisionTreeNode root;


    public DecisionTree(DecisionTreeNode root) {
        this.root = root;
    }


    public String predict(Dataset.Datapoint point) {
        DecisionTreeNode current = this.root;
        while (!current.isLeaf()) {
            AttributeNode currentAttribute = (AttributeNode) current;
            String feature = point.attributes.get(currentAttribute.attribute);
            current = currentAttribute.children.get(feature);
        }
        return ((OutcomeNode) current).outcome;
    }


    public static DecisionTreeNode id3helper(Dataset data, List<String> attributes) {
        // gets most common outcome
        String same = data.pointsHaveSameOutcome();
        // if all data points have the same outcome
        if (!same.equals("")) {
            // returns a decision tree with that uniform common outcome node
            return new OutcomeNode(same);
            // if no remaining attributes
        } else if (attributes.isEmpty()) {
            // returns a decision tree with the most common outcome node
            return new OutcomeNode(data.getMostCommonOutcome());
        }

        // finds the lowest entropy attribute
        String lowestEntropyAttribute = data.getAttributeWithMinEntropy(attributes);
        // gets the features of the lowest entropy attribute
        List<String> features = data.getFeaturesForAttribute(lowestEntropyAttribute);
        // makes a mutable copy of attributes and removes from it the lowest entropy attribute
        List<String> mutableAttributes = new ArrayList<>(attributes);
        mutableAttributes.remove(lowestEntropyAttribute);
        // makes attribute node for lowest entropy attribute
        AttributeNode a = new AttributeNode(lowestEntropyAttribute);
        // loops through each feature
        for (String feature : features) {
            // gets all the datapoints with the current feature
            Dataset datapoints = data.getPointsWithFeature(feature);
            // if there are no datapoints with the current feature
            if (datapoints.isEmpty()) {
                // enters into the map(feature, mostCommonOutcome node)
                a.children.put(feature, new OutcomeNode(data.getMostCommonOutcome()));
                // if there are datapoints with the current feature
            } else {
                // // enters into the map(feature, recursively made child)
                //List<String> temp = new ArrayList<>(attributes);
                a.children.put(feature, id3helper(datapoints, mutableAttributes));
            }
        }
        return a;
    }

    
    public static DecisionTree id3(Dataset dataset, List<String> attributes) {
        return new DecisionTree(id3helper(dataset, attributes));
    }
}
