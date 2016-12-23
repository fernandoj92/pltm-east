package org.latlab.learner.geast.operators;

import java.util.LinkedList;

import org.latlab.graph.AbstractNode;
import org.latlab.learner.geast.IModelWithScore;
import org.latlab.learner.geast.context.ISearchOperatorContext;
import org.latlab.model.BeliefNode;
import org.latlab.model.Builder;
import org.latlab.model.DiscreteBeliefNode;
import org.latlab.util.DiscreteVariable;

public class StateDeletor extends SearchOperatorImpl {

	public static class Candidate extends
			org.latlab.learner.geast.operators.SearchCandidate {

		public final DiscreteVariable oldVariable;
		public final DiscreteVariable newVariable;

		private final static String ELEMENT = "deleteState";
		private static final String OPERATOR_NAME = "SD";
		private final static String ATTRIBUTES_FORMAT =
				"node='%s' original='%d'";

		protected Candidate(IModelWithScore base, DiscreteVariable target) {
			super(base);

			oldVariable = target;
			newVariable =
					new DiscreteVariable(oldVariable.getCardinality() - 1);

			DiscreteBeliefNode newNode =
					Builder.replaceVariable(structure, oldVariable, newVariable);

			modification.add(newVariable);
			for (AbstractNode child : newNode.getChildren()) {
				modification.add(((BeliefNode) child).getVariable());
			}
		}

		@Override
		public String element() {
			return ELEMENT;
		}

		@Override
		public String attributes() {
			return String.format(ATTRIBUTES_FORMAT, oldVariable.getName(),
					oldVariable.getCardinality());
		}

		@Override
		public String name() {
			return "StateDeletionCandidate";
		}

		@Override
		public String operatorName() {
			return OPERATOR_NAME;
		}
	}

	public StateDeletor(ISearchOperatorContext context) {
		super(context);
	}

	@Override
	protected LinkedList<SearchCandidate> generateCandidates(
			IModelWithScore base) {
		LinkedList<SearchCandidate> candidates =
				new LinkedList<SearchCandidate>();

		for (DiscreteBeliefNode node : base.model().getInternalNodes()) {
			// skip this latent variable if it has reached the minimum
			// cardinality
			if (node.getVariable().getCardinality() <= 2)
				continue;

			Candidate candidate = new Candidate(base, node.getVariable());
			candidates.add(candidate);
		}

		return candidates;
	}

}
