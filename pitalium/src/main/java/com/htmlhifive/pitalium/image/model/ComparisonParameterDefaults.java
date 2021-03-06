/*
 * Copyright (C) 2015-2017 NS Solutions Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.htmlhifive.pitalium.image.model;

/**
 * Default parameter setting for comparison
 */
public class ComparisonParameterDefaults {

	/* default setting for comparison parameters */

	// recognizing threshold for sub-pixel rendered font
	private static double subpixelRateThreshold = 0.63;
	private static double subpixelPerLineThreshold = 0.63;

	// difference threshold to ignore small differences
	private static double diffThreshold = 0.1;

	// if similarityThresDiff is bigger than this threshold, categorize the object as "SHIFT"
	// only applied to two rectangle objects.
	private static double shiftSimilarityThreshold = 0.98;

	// parameters for categorization of scaling
	private static double maximumScaleFactor = 1.2;
	private static double scalingFeatureCriterion = 0.9;

	// group distance for building different area
	private static int defaultGroupDistance = 10;
	private static int splitGroupDistance = 6;

	// maximum range for shift checking, similarity calculation
	private static int maxShift = 10;
	private static int maxMove = 5; // moving range for similarity calculation

	/**
	 * Constructor
	 */
	public ComparisonParameterDefaults() {
	};

	public static double getSubpixelRateThreshold() {
		return subpixelRateThreshold;
	}

	public static void setSubpixelRateThreshold(double subpixelRateThreshold) {
		ComparisonParameterDefaults.subpixelRateThreshold = subpixelRateThreshold;
	}

	public static double getSubpixelPerLineThreshold() {
		return subpixelPerLineThreshold;
	}

	public static void setSubpixelPerLineThreshold(double subpixelPerLineThreshold) {
		ComparisonParameterDefaults.subpixelPerLineThreshold = subpixelPerLineThreshold;
	}

	public static void setDiffThreshold(double threshold) {
		diffThreshold = threshold;
	}

	public static double getDiffThreshold() {
		return diffThreshold;
	}

	public static void setShiftSimilarityThreshold(double threshold) {
		shiftSimilarityThreshold = threshold;
	}

	public static double getShiftSimilarityThreshold() {
		return shiftSimilarityThreshold;
	}

	public static void setMaximumScaleFactor(double scaleFactor) {
		maximumScaleFactor = scaleFactor;
	}

	public static double getMaximumScaleFactor() {
		return maximumScaleFactor;
	}

	public static void setScalingFeatureCriterion(double criterion) {
		scalingFeatureCriterion = criterion;
	}

	public static double getScalingFeatureCriterion() {
		return scalingFeatureCriterion;
	}

	public static void setDefaultGroupDistance(int group_distance) {
		defaultGroupDistance = group_distance;
	}

	public static int getDefaultGroupDistance() {
		return defaultGroupDistance;
	}

	public static void setSplitGroupDistance(int group_distance) {
		splitGroupDistance = group_distance;
	}

	public static int getSplitGroupDistance() {
		return splitGroupDistance;
	}

	public static void setMaxShift(int max) {
		maxShift = max;
	}

	public static int getMaxShift() {
		return maxShift;
	}

	public static void setMaxMove(int max) {
		maxMove = max;
	}

	public static int getMaxMove() {
		return maxMove;
	}

}
