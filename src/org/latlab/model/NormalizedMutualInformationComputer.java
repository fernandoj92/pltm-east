package org.latlab.model;

import java.util.Arrays;

import org.latlab.reasoner.NaturalCliqueTreePropagation;
import org.latlab.util.Function;
import org.latlab.util.Utils;

/**
 * Computes normalized mutual information between variables in a Bayesian
 * network.
 * 
 * @author leonard
 * 
 */
public class NormalizedMutualInformationComputer {

	private final NaturalCliqueTreePropagation ctp;

	public NormalizedMutualInformationComputer(Gltm model) {
		ctp = new NaturalCliqueTreePropagation(model);
		ctp.propagate();
	}

	/**
	 * Computes and returns the normalized mutual information between two belief
	 * nodes.
	 * 
	 * @param node1
	 *            first node
	 * @param node2
	 *            second node
	 * @return normalized mutual information between two nodes
	 */
	public double compute(DiscreteBeliefNode node1, DiscreteBeliefNode node2) {
		Function jointProbability = ctp.getMarginal(Arrays.asList(
				node1.getVariable(), node2.getVariable()));
		return Utils.computeNormalizedMutualInformation(jointProbability);
	}
}
