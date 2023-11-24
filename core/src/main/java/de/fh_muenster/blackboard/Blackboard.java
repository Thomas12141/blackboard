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
package de.fh_muenster.blackboard;

import de.fh_muenster.blackboard.internal.BasicBlackboard;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.ServiceLoader;
import java.util.concurrent.Future;

/**
 * Interface of the blackboard abstraction.
 */
public interface Blackboard {


	/**
	 * Very simple logging utility method.
	 * 
	 * @param fmt  format for the arguments
	 * @param args objects to log
	 */
	static void log(String fmt, Object... args) {
		System.out.println(String.format(fmt, args));
	}

	/**
	 * Access to a blackboard implementation via reflection API.
	 * 
	 * @return Blackboard instance
	 */
	static Blackboard getInstance() {
		ServiceLoader<Blackboard> loader = ServiceLoader.load(Blackboard.class);
		return loader.findFirst().orElseThrow(() -> new IllegalStateException("no blackboad found"));
	}

	/**
	 * Register a KnowledgeSource using generic problem and solution.
	 * 
	 * @param ks knowledge-source to register
	 */
	<Problem, Solution> void register(KnowledgeSource<Problem, Solution> ks);

	/**
	 * Write a problem on the blackboard and return a solution handle. The solution
	 * might be calculated in the future with different knowledge sources and
	 * provide different partial answers.
	 * 
	 * @param task to solve
	 * @return solution wrapped within a future object
	 */
	Future<?> write(Object task);

	/**
	 * Get all found answers of a written task.
	 * 
	 * @param task which produces the answers.
	 * @return all found answers.
	 */
	Collection<?> answers(Object task);

	/**
	 * Filter a type-specific found answer of a written task.
	 * 
	 * @param task which produces the answers
	 * @param type of the answer uses as a filter
	 * @return found answer of given type
	 */
	default <T> T answer(Class<T> type, Object task) {
		Collection<?> answers = answers(task);
		log("task answers:%s  => %s", task, answers);
		for (Object answer : answers) {
			if (type.isAssignableFrom(answer.getClass())) {
				return type.cast(answer);
			}
		}
		throw new IllegalArgumentException("no answer of type " + type);
	}

}
