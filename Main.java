package myColt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import cern.colt.function.DoubleDoubleFunction;
import cern.colt.function.DoubleFunction;
import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;

public class Main {

	public static void main(String[] args) throws Exception {

		Main main = new Main();
		DoubleMatrix2D X = main.readData("input.dat", 2);
		DoubleMatrix2D Y = main.readData("output.dat", 1);
		DoubleMatrix2D B = main.IRLS(Y, X, 40, 0.0001f, 0.001f);
		System.out.println("B=" + B);
	}

	public DoubleMatrix2D IRLS(DoubleMatrix2D Y, DoubleMatrix2D X, int maxiter,
			float d, float tolerance) {
		int row = X.rows();
		DoubleMatrix2D delta = new DenseDoubleMatrix2D(1, row);
		delta.assign(d);

		DoubleMatrix1D tempW = new DenseDoubleMatrix1D(row);
		tempW.assign(1);
		DoubleMatrix2D w = new DenseDoubleMatrix2D(1, row);
		w.assign(1);
		DoubleMatrix2D W = DoubleFactory2D.dense.diagonal(tempW);

		DoubleMatrix2D t1 = Algebra.DEFAULT.mult(
				(Algebra.DEFAULT.mult(Algebra.DEFAULT.transpose(X), W)), X);
		DoubleMatrix2D t2 = Algebra.DEFAULT.mult(
				(Algebra.DEFAULT.mult(Algebra.DEFAULT.transpose(X), W)), Y);
		DoubleMatrix2D B = Algebra.DEFAULT
				.mult(Algebra.DEFAULT.inverse(t1), t2);

		DoubleDoubleFunction maximum = new DoubleDoubleFunction() {
			public double apply(double a, double b) {
				return a > b ? a : b;
			}
		};

		DoubleFunction reciprocal = new DoubleFunction() {
			public double apply(double a) {
				return (float) 1 / a;
			}
		};
		DoubleMatrix2D _B = null;
		DoubleMatrix2D _w = null;
		DoubleMatrix2D _delta = null;
		double tol = 0d;
		for (int i = 0; i < maxiter; ++i) {
			_B = B;
			_delta = delta.copy();
			_w = Algebra.DEFAULT.transpose(minusAndAbs(Y,
					Algebra.DEFAULT.mult(X, B)));
			System.out.println("i=" + i);
			w = _delta.assign(_w, maximum).assign(reciprocal);
			tempW = new DenseDoubleMatrix1D(row);
			int col = w.columns();
			double[] dArr = new double[col];
			for (int m = 0; m < w.rows(); ++m) {
				for (int n = 0; n < col; ++n) {
					dArr[n] = w.get(m, n);
				}
			}
			tempW.assign(dArr);
			W = DoubleFactory2D.dense.diagonal(tempW);

			t1 = Algebra.DEFAULT.mult(
					(Algebra.DEFAULT.mult(Algebra.DEFAULT.transpose(X), W)), X);
			t2 = Algebra.DEFAULT.mult(
					(Algebra.DEFAULT.mult(Algebra.DEFAULT.transpose(X), W)), Y);
			B = Algebra.DEFAULT.mult(Algebra.DEFAULT.inverse(t1), t2);

			tol = sum(minusAndAbs(B, _B));
			System.out.println("tol=");
			System.out.println(tol);
			if (tol < tolerance) {
				return B;
			}
		}
		return B;
	}

	private double sum(DoubleMatrix2D matrix) {
		double s = 0d;
		int col = matrix.columns();
		int row = matrix.rows();
		for (int i = 0; i < row; ++i) {
			for (int j = 0; j < col; ++j) {
				s += matrix.get(i, j);
			}
		}
		return s;
	}

	private DoubleMatrix2D minusAndAbs(DoubleMatrix2D matrix1,
			DoubleMatrix2D matrix2) {
		int col = matrix1.columns();
		int row = matrix1.rows();
		DoubleMatrix2D doubleMatrix2D = new DenseDoubleMatrix2D(row, col);
		for (int i = 0; i < row; ++i) {
			for (int j = 0; j < col; ++j) {
				doubleMatrix2D.set(i, j,
						Math.abs(matrix1.get(i, j) - matrix2.get(i, j)));
			}
		}
		return doubleMatrix2D;
	}

	public DoubleMatrix2D readData(String name, int col) throws Exception {
		DoubleMatrix2D matrix = null;
		String line = null;
		List<String> list = new ArrayList<>();
		BufferedReader buffer = new BufferedReader(new FileReader(name));
		try {
			while ((line = buffer.readLine()) != null) {
				list.add(line);
			}
			int size = list.size();
			matrix = new DenseDoubleMatrix2D(size, col);
			for (int i = 0; i < size; ++i) {
				String str = list.get(i);
				String item[] = StringUtils.split(str);
				for (int j = 0; j < col; ++j) {
					matrix.set(i, j, Double.parseDouble(item[j]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try {
				buffer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return matrix;
	}
}
