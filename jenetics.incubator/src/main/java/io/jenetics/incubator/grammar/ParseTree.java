/*
 * Java Genetic Algorithm Library (@__identifier__@).
 * Copyright (c) @__year__@ Franz Wilhelmstötter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author:
 *    Franz Wilhelmstötter (franz.wilhelmstoetter@gmail.com)
 */
package io.jenetics.incubator.grammar;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.jenetics.incubator.grammar.Cfg.NonTerminal;
import io.jenetics.incubator.grammar.Cfg.Symbol;

import io.jenetics.ext.util.TreeNode;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public final class ParseTree {
	private ParseTree() {}

	public static TreeNode<Symbol> generate(
		final Cfg cfg,
		final SymbolIndex index
	) {
		final NonTerminal start = cfg.start();
		final TreeNode<Symbol> symbols = TreeNode.of(start);

		boolean expanded = true;
		while (expanded) {
			final Optional<TreeNode<Symbol>> tree = symbols.leaves()
				.filter(leaf -> leaf.value() instanceof NonTerminal)
				.filter(leaf -> cfg.rule((NonTerminal)leaf.value()).isPresent())
				.findFirst();

			tree.ifPresent(t ->
				expand(cfg, (NonTerminal)t.value(), index).forEach(t::attach)
			);

			expanded = tree.isPresent();
		}

		return symbols;
	}

	static List<Symbol> expand(
		final Cfg cfg,
		final NonTerminal symbol,
		final SymbolIndex index
	) {
		return cfg.rule(symbol)
			.map(rule -> rule.alternatives().get(index.next(rule)).symbols())
			.orElse(List.of());
	}

	public static TreeNode<Symbol> apply(
		final Cfg cfg,
		final SymbolIndex index
	) {
		TreeNode<Symbol> tree = TreeNode.of(cfg.start());

		int currentCodonIndex = 0;
		int wraps = 0;
		while (true) {
			TreeNode<Symbol> treeToBeReplaced = null;

			final var it = tree.leaves().iterator();
			while (it.hasNext()) {
				TreeNode<Symbol> node = it.next();

				if (node.value() instanceof NonTerminal nt && cfg.rule(nt).isPresent()) {
				//if (cfg.rules().stream().anyMatch(r -> r.start().equals(node.value()))) {
				//if (grammar.getRules().keySet().contains(node.content())) {
					treeToBeReplaced = node;
					break;
				}
			}

			if (treeToBeReplaced == null) {
				break;
			}

//			//get codon index and option
//			if ((currentCodonIndex + 1) * codonLength > genotype.size()) {
//				wraps = wraps + 1;
//				currentCodonIndex = 0;
//				if (wraps > maxWraps) {
//					throw new IllegalArgumentException(String.format("Too many wraps (%d>%d)", wraps, maxWraps));
//				}
//			}

			final var value = treeToBeReplaced.value();
//			final var options = cfg.rules().stream().filter(r -> r.start().equals(value)).findFirst()
//				.map(Rule::alternatives)
//				.orElse(List.of());

//			//List<List<T>> options = grammar.getRules().get(treeToBeReplaced.content());
//			int optionIndex = 0;
//			if (options.size() > 1) {
//				optionIndex = genotype.slice(currentCodonIndex * codonLength, (currentCodonIndex + 1) * codonLength).toInt() % options.size();
//				currentCodonIndex = currentCodonIndex + 1;
//			}

			//add children
			if (value instanceof NonTerminal nt) {
				final var alternatives = cfg.rule(nt)
					.map(rule -> rule.alternatives().get(index.next(rule)).symbols())
					.orElse(List.of());

				for (Symbol t : alternatives) {
					TreeNode<Symbol> newChild = TreeNode.of(t);
					treeToBeReplaced.attach(newChild);
				}
			}
		}

		return tree;
	}

	static <T> List<TreeNode<T>> leaves(final TreeNode<T> node) {
		if (node.childCount() == 0)  {
			return List.of(node);
		} else {
			final var leaves = new ArrayList<TreeNode<T>>();
			node.childStream().forEach(n -> leaves.addAll(leaves(n)));
			return leaves;
		}
	}

//	public List<Tree<C>> leaves() {
//		if (children.isEmpty()) {
//			return List.of(this);
//		}
//		List<Tree<C>> leaves = new ArrayList<>();
//		children.forEach(c -> leaves.addAll(c.leaves()));
//		return leaves;
//	}

}
