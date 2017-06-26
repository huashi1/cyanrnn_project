/**   
 * @package	com.kingwang.cdmrnn.utils
 * @File		AttBatchDerivative.java
 * @Crtdate	Oct 2, 2016
 *
 * Copyright (c) 2016 by <a href="mailto:wangyongqing.casia@gmail.com">King Wang</a>.   
 */
package com.kingwang.netrnn.batchderv.impl;

import java.io.Serializable;
import java.util.Map;

import org.jblas.DoubleMatrix;

import com.kingwang.netrnn.batchderv.BatchDerivative;
import com.kingwang.netrnn.cons.AlgConsHSoftmax;

/**
 *
 * @author King Wang
 * 
 * Oct 2, 2016 8:29:49 PM
 * @version 1.0
 */
public class OutputBatchHasXWithHSoftmaxDerivative implements BatchDerivative, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6945194770004264043L;

	public DoubleMatrix[] dWxy;
	public DoubleMatrix[] dWdy;
	public DoubleMatrix[] dWsy;
	public DoubleMatrix[] dby;
	
	public DoubleMatrix dWxc;
	public DoubleMatrix dWdc;
	public DoubleMatrix dWsc;
	public DoubleMatrix dbsc;
	
	public void clearBatchDerv() {
		dWxy = null;
		dWdy = null;
		dWsy = null;
		dby = null;
		
		dWxc = null;
		dWdc = null;
		dWsc = null;
		dbsc = null;
	}

	public void batchDervCalc(Map<String, DoubleMatrix> acts, double avgFac) {
		DoubleMatrix[] _dWxy = new DoubleMatrix[AlgConsHSoftmax.cNum];
		DoubleMatrix[] _dWdy = new DoubleMatrix[AlgConsHSoftmax.cNum];
		DoubleMatrix[] _dWsy = new DoubleMatrix[AlgConsHSoftmax.cNum];
		DoubleMatrix[] _dby = new DoubleMatrix[AlgConsHSoftmax.cNum];
		
		if(dWxy==null || dWdy==null || dWsy==null || dby==null) {
			dWxy = new DoubleMatrix[AlgConsHSoftmax.cNum];
			dWdy = new DoubleMatrix[AlgConsHSoftmax.cNum];
			dWsy = new DoubleMatrix[AlgConsHSoftmax.cNum];
			dby = new DoubleMatrix[AlgConsHSoftmax.cNum];
		}
		
		for(int c=0; c<AlgConsHSoftmax.cNum; c++) {
			if(!acts.containsKey("dWsy"+c) || !acts.containsKey("dby"+c)
					|| !acts.containsKey("dWxy"+c) || !acts.containsKey("dWdy"+c)) {
				continue;
			}
			_dWxy[c] = acts.get("dWxy"+c);
			_dWdy[c] = acts.get("dWdy"+c);
			_dWsy[c] = acts.get("dWsy"+c);
			_dby[c] = acts.get("dby"+c);
			 
			if(dWxy[c]==null || dWdy[c]==null || dWsy[c]==null || dby[c]==null) {
				dWxy[c] = new DoubleMatrix(_dWxy[c].rows, _dWxy[c].columns);
				dWdy[c] = new DoubleMatrix(_dWdy[c].rows, _dWdy[c].columns);
				dWsy[c] = new DoubleMatrix(_dWsy[c].rows, _dWsy[c].columns);
				dby[c] = new DoubleMatrix(_dby[c].rows, _dby[c].columns);
			}
			 
			dWxy[c] = dWxy[c].add(_dWxy[c]).mul(avgFac);
			dWdy[c] = dWdy[c].add(_dWdy[c]).mul(avgFac);
			dWsy[c] = dWsy[c].add(_dWsy[c]).mul(avgFac);
			dby[c] = dby[c].add(_dby[c]).mul(avgFac);
		}
		
		DoubleMatrix _dWxc = acts.get("dWxc");
		DoubleMatrix _dWdc = acts.get("dWdc");
		DoubleMatrix _dWsc = acts.get("dWsc");
		DoubleMatrix _dbsc = acts.get("dbsc");
		 
		if(dWxc==null || dWdc==null || dWsc==null || dbsc==null) {
			dWxc = new DoubleMatrix(_dWxc.rows, _dWxc.columns);
			dWdc = new DoubleMatrix(_dWdc.rows, _dWdc.columns);
			dWsc = new DoubleMatrix(_dWsc.rows, _dWsc.columns);
			dbsc = new DoubleMatrix(_dbsc.rows, _dbsc.columns);
		}
		 
		dWxc = dWxc.add(_dWxc).mul(avgFac);
		dWdc = dWdc.add(_dWdc).mul(avgFac);
		dWsc = dWsc.add(_dWsc).mul(avgFac);
		dbsc = dbsc.add(_dbsc).mul(avgFac);
	}
}