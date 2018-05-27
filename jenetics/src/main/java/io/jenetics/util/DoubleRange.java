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
package io.jenetics.util;

import static java.lang.String.format;

import java.io.Serializable;

/**
 * Double range class.
 *
 * @implNote
 * This class is immutable and thread-safe.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version 3.2
 * @since 3.2
 */
public final class DoubleRange implements Serializable {

	private static final long serialVersionUID = 1L;

	private final double _min;
	private final double _max;

	private DoubleRange(final double min, final double max) {
		if (min > max) {
			throw new IllegalArgumentException(format(
				"Min greater than max: %s > %s", min, max
			));
		}

		_min = min;
		_max = max;
	}

	/**
	 * Return the minimum value of the double range.
	 *
	 * @return the minimum value of the double range
	 */
	public double getMin() {
		return _min;
	}

	/**
	 * Return the maximum value of the double range.
	 *
	 * @return the maximum value of the double range
	 */
	public double getMax() {
		return _max;
	}

	/**
	 * Returns the range of this {@code DoubleRange} as a {@code IntRange} after
	 * a narrowing primitive conversion.
	 *
	 * @since !__version__!
	 *
	 * @return this {@code DoubleRange} as {@code IntRange}
	 */
	public IntRange intRange() {
		return IntRange.of((int)_min, (int)_max);
	}

	/**
	 * Returns the range of this {@code DoubleRange} as a {@code LongRange} after
	 * a narrowing primitive conversion.
	 *
	 * @since !__version__!
	 *
	 * @return this {@code DoubleRange} as {@code LongRange}
	 */
	public LongRange longRange() {
		return LongRange.of((long)_min, (long)_max);
	}

	/**
	 * Create a new {@code DoubleRange} object with the given {@code min} and
	 * {@code max} values.
	 *
	 * @param min the lower bound of the double range
	 * @param max the upper bound of the double range
	 * @return a new {@code DoubleRange} object
	 * @throws IllegalArgumentException if {@code min > max}
	 */
	public static DoubleRange of(final double min, final double max) {
		return new DoubleRange(min, max);
	}

	@Override
	public int hashCode() {
		int hash  = 17;
		hash += 31*Double.hashCode(_max) + 37;
		hash += 31*Double.hashCode(_min) + 37;
		return hash;
	}

	@Override
	public boolean equals(final Object obj) {
		return obj == this ||
			obj instanceof DoubleRange &&
			Double.compare(_min, ((DoubleRange)obj)._min) == 0 &&
			Double.compare(_max, ((DoubleRange)obj)._max) == 0;
	}

	@Override
	public String toString() {
		return "[" + _min + ", " + _max + "]";
	}

}
