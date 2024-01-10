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

import com.sun.jna.Library;
import com.sun.jna.Native;

import java.util.function.Function;

import static java.lang.Math.abs;
import static java.lang.String.format;

/**
 * Numerical differentiation of real valued functions.
 * @author nwulff
 * @since  08.12.2023
 *
 */
public class Integrator {
	//static final String PATH = "java.library.path";
	static final String PATH = "jna.library.path";
	static final String LIBNAME = "analysis";
	public static final double PRECISION = 1.E-8;
	public static final int MAX_ITERATIONS= 16;
	public static final double H0=1.E-4;
	private static boolean loaded = false;
	private boolean useNative;
	private static JNAIntegrator cdintegrator;
	/**
	 * Native Implementation interface for JNA to
	 * decorate a C shared library.
	 */
	static interface JNAIntegrator extends Library {
		JNAIntegrator INSTANCE = instance();
		/**
		 * Access to the C-library via JNA.
		 * @return a C library Java wrapper
		 */
		static JNAIntegrator instance() {
			if(null == INSTANCE)  {
				return Native.load(LIBNAME, JNAIntegrator.class);
			}
			return INSTANCE;
		}
		/** just  a quick check if the library is loaded at all. */
		int isLoaded();
		/** the C integrate method via JNA proxy.     */
		double integrate(Fct f, double a, double b, double delta);
	}

	static {
		try {
			// for use with JNI...
			//System.loadLibrary(LIBNAME);
			cdintegrator = JNAIntegrator.instance();
			loaded = cdintegrator.isLoaded()>0;
		} catch(Throwable error) {
			System.out.printf("%s: %s%n",PATH,System.getProperty(PATH));
			System.out.printf("failed to load %s : %s%n",LIBNAME,error.getMessage());
			//error.printStackTrace();
		} finally {
			//System.out.printf("Integrator library %s loaded %b%n",LIBNAME,loaded);
		}
	}
	/**
	 * POJO constructor.
	 */
	public Integrator() {
		this(true);
	}
	/**
	 * Constructor with native or java implementation flag.
	 * @param useJava signal if pure Java usage
	 */
	public Integrator(boolean useJava) {
		useNative = !useJava;
	}
	/**
	 * Calculate numerical the derivative function.
	 * @param f function, which must be single valued.
	 * @return f'
	 */
	public Function<double[],Double> integral(Function<double[],Double > f) {
		return (x)->integral(f,x);
	}
	/**
	 * Calculate the numerical derivative f'(x) at point x.
	 * @param f function to use
	 * @param x argument which has to be single valued
	 * @return f'(x)
	 */
	public double integral(Function<double[],Double > f, double[] x) {
		return integrate(f,x,PRECISION);
	}
	/**
	 * Calculate the numerical derivative f'(x) at point x.
	 * @param f function to use
	 * @param x argument which has to be single valued
	 * @param precision to reach
	 * @return f'(x) within delta precision
	 */
	public double integrate(Function<double[],Double > f, double[] x, double precision) {
		Fct fct = Fct.asFct(f);
		if(useNative) {
			try {
				return cdintegrator.integrate(fct,x[0], x[1],precision);
			} catch(RuntimeException error) {
				System.err.printf("%s %s%n",error,error.getCause());
				throw error;
			}
		}
		return integrateInJava(fct,x[0], x[1],precision);
	}
	/**
	 * Basic iterative algorithm in Java to calculate a numerical
	 * derivative f'(x) at point x.
	 * @param fct function to differentiate.
	 * @param b upper border
	 * @param a lower border
	 * @param precision to reach
	 * @return f'(x)
	 */
	double integrateInJava(final Fct fct, final double a, final double b, final double precision) {
		double tn = (fct.f(a)+fct.f(b))/2.0;// n = 0
		double t2n = tn*(b-a); // n = 1
		double diff;
		int n = 1;
		do{
			diff = Math.abs(t2n-tn);
			n*= 2;
			if(n<0){
				break;
			}
			tn = t2n;
			double temp = 0;
			double h = (b-a)/n;
			for (int i = 0; i < n; ++i) {
				temp+= fct.f(a + h/2.0 + i*h);
			}
			temp*=h;
			t2n = (tn +temp)/2.0;
			if(diff <= Math.abs(t2n - tn)){
				break;
			}
		} while (diff >= precision);
		return (4*t2n-tn)/3.0;
	}
} 
