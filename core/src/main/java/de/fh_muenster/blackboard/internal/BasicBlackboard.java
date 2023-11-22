/*
 * Project: core
 *
 * Copyright (c) 2023, Prof. Dr. Nikolaus Wulff University of Applied Sciences,
 * Muenster, Germany Lab for computer sciences (Lab4Inf).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package de.fh_muenster.blackboard.internal;

import static de.fh_muenster.blackboard.Blackboard.log;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Function;

import de.fh_muenster.blackboard.Blackboard;
import de.fh_muenster.blackboard.KnowledgeSource;

/**
 * Blackboard mono state implementation.
 */
public final class BasicBlackboard implements Blackboard {
	private static final BasicBlackboard INSTANCE = new BasicBlackboard();
	private static final Set<KnowledgeSource<?, ?>> SOLVERS = new HashSet<>();
	private static final Map<Object, CompletingFuture> TASKS = new HashMap<>();

	public static Type answerType;

	/**
	 * Static provider method for the ServiceLoader.
	 * 
	 * @return Blackboard instance
	 */
	public static Blackboard provider() {
		return INSTANCE;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.fh_muenster.blackboard.Blackboard#register(de.fh_muenster.blackboard.KnowledgeSource)
	 */
	@Override
	public <Problem, Solution> void register(KnowledgeSource<Problem, Solution> ks) {
		if (!SOLVERS.contains(ks)) {
			SOLVERS.add(ks);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.fh_muenster.blackboard.Blackboard#write(java.lang.Object)
	 */
	@Override
	public Future<?> write(Object task) {
		CompletingFuture future = getFuture(task);
		return write(future, task);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.fh_muenster.blackboard.Blackboard#answers(java.lang.Object)
	 */
	@Override
	public Collection<?> answers(Object task) {
		if (!TASKS.containsKey(task)) {
			write(task);
		}
		CompletingFuture future = TASKS.remove(task);
		return future.solutions;
	}

	/**
	 * internal write method chaining different partial solutions to an assigned
	 * task.
	 * 
	 * @param future result
	 * @param task   arising
	 * @return
	 */
	private CompletingFuture write(CompletingFuture future, Object task) {
		for (KnowledgeSource<?, ?> ks : SOLVERS) {
			if (ks.canHandle(this, task)) {
				try {
					Object answer = ks.solve(this, task);
					log("%s: %s => %s", ks.getClass().getSimpleName(), task, answer);
					future.solutions.add(answer);
					if (!(answer instanceof Number || answer instanceof String || answer instanceof Function<?, ?>)) {
						write(future, answer);
					}
				} catch (RuntimeException e) {
					throw e;
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		// All possible KS are done ...
		future.complete(future.solutions);
		return future;
	}

	private CompletingFuture getFuture(Object task) {
		CompletingFuture future;
		if (TASKS.containsKey(task)) {
			future = TASKS.get(task);
		} else {
			future = new CompletingFuture(task);
			TASKS.put(task, future);
		}
		return future;
	}

	private final static class CompletingFuture extends CompletableFuture<Object> {
		private final Object problem;
		final Collection<Object> solutions;

		CompletingFuture(Object task) {
			solutions = new ArrayList<>();
			problem = Objects.requireNonNull(task, "task is null");
		}

		@Override
		public String toString() {
			return String.format("%s => %s", problem.toString(), solutions);
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return problem.hashCode();
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof CompletingFuture that) {
				return this.problem.equals(that.problem);
			}
			return false;
		}
	}
}
