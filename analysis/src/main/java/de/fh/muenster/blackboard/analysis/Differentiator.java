/*
 * Project: analysis
 *
 * Copyright (c) 2023,  Prof. Dr. Nikolaus Wulff
 * University of Applied Sciences, Muenster, Germany
 * Lab for computer sciences (Lab4Inf).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package de.fh.muenster.blackboard.analysis;
import static java.lang.Math.*;
import static java.lang.String.format;
import java.util.function.Function;
import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * Numerical differentiation of real valued functions.
 * @author nwulff
 * @since  08.12.2023
 *
 */
public class Differentiator {
	//static final String PATH = "java.library.path";
	static final String PATH = "jna.library.path";
	static final String LIBNAME = "analysis";
	public static final double PRECISION = 1.E-8;
	public static final int MAX_ITERATIONS= 16;
	public static final double H0=1.E-4;
	private static boolean loaded = false;
	private boolean useNative;
	private static JNADifferentiator cdifferentiator;
	/**
	 * Native Implementation interface for JNA to
	 * decorate a C shared library.
	 */
	static interface JNADifferentiator extends Library {
		JNADifferentiator INSTANCE = instance();
		/**
		 * Access to the C-library via JNA.
		 * @return a C library Java wrapper
		 */
		static JNADifferentiator instance() {
			if(null == INSTANCE)  {
				return Native.load(LIBNAME,JNADifferentiator.class);
			}
			return INSTANCE;
		}
		/** just  a quick check if the library is loaded at all. */
		int isLoaded();
		/** the C differentiate method via JNA proxy.     */
		double differentiate(Fct f, double x, double delta);		
	}
	
	static {
		try {
			// for use with JNI...
			//System.loadLibrary(LIBNAME);
			cdifferentiator = JNADifferentiator.instance();
			loaded = cdifferentiator.isLoaded()>0;
		} catch(Throwable error) {
			System.out.printf("%s: %s%n",PATH,System.getProperty(PATH));
			System.out.printf("failed to load %s : %s%n",LIBNAME,error.getMessage());
			//error.printStackTrace();
		} finally {
			//System.out.printf("Differentiator library %s loaded %b%n",LIBNAME,loaded);
		}
	}
	/**
	 * POJO constructor. 
	 */
	public Differentiator() {
		this(true);
	}
	/**
	 * Constructor with native or java implementation flag.
	 * @param useJava signal if pure Java usage
	 */
	public Differentiator(boolean useJava) {
		useNative = !useJava;
	}
	/**
	 * Calculate numerical the derivative function.
	 * @param f function, which must be single valued.
	 * @return f'
	 */
	public Function<double[],Double> differential(Function<double[],Double > f) {
		return (x)->differentiate(f,x);
	}
	/**
	 * Calculate the numerical derivative f'(x) at point x.
	 * @param f function to use
	 * @param x argument which has to be single valued
	 * @return f'(x)
	 */
	public double differentiate(Function<double[],Double > f, double[] x) {
		return differentiate(f,x,PRECISION);
	}
	/**
	 * Calculate the numerical derivative f'(x) at point x.
	 * @param f function to use
	 * @param x argument which has to be single valued
	 * @param precision to reach
	 * @return f'(x) within delta precision
	 */
	public double differentiate(Function<double[],Double > f, double[] x, double precision) {
		if(1 != x.length) {
			throw new IllegalArgumentException("function not single valued!");
		}
		Fct fct = Fct.asFct(f);
		if(useNative) {
			try {
				return cdifferentiator.differentiate(fct,x[0],precision);
			} catch(RuntimeException error) {
				System.err.printf("%s %s%n",error,error.getCause());
				throw error;
			}
		}
		return differentiateInJava(fct,x[0],precision);
	}
	/**
	 * Basic iterative algorithm in Java to calculate a numerical
	 * derivative f'(x) at point x.
	 * @param fct function to differentiate.
	 * @param x argument
	 * @param precision to reach
	 * @return f'(x)
	 */
	double differentiateInJava(final Fct fct, final double x, final double precision) {
		double h = H0,diff,fxp,fxm,dfo,df=Double.MAX_VALUE;
		int iteration = 0;
		if(precision<=0) {
			throw new IllegalArgumentException(format("none positive precision %f",precision));
		}
		do {
			iteration++;
			dfo = df;
			fxp = fct.f(x+h);
			fxm = fct.f(x-h);
			// calculate the slope of the tangent via a narrowing secant 
			df = (fxp - fxm)/(2*h);
			diff = abs(df-dfo);
			if(abs(df)>1)diff/=abs(df);
			h /=2;
		} while(diff>precision && iteration<MAX_ITERATIONS);
		if(iteration>=MAX_ITERATIONS) {
			throw new ArithmeticException(format("no convergence err:%.3g",diff));
		}
		return df;
	}
	/**
	 * Native implementation via JNI, if not using JNA.
	 * @param f
	 * @param x
	 * @param delta
	 * @return
	 */
	 native double differentiateInC(Fct f, double x, double delta);
} 
