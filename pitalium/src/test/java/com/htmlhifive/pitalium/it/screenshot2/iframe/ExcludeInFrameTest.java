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

package com.htmlhifive.pitalium.it.screenshot2.iframe;

import com.google.common.base.Supplier;
import com.htmlhifive.pitalium.core.model.ScreenshotArgument;
import com.htmlhifive.pitalium.core.model.TargetResult;
import com.htmlhifive.pitalium.core.selenium.PtlWebElement;
import com.htmlhifive.pitalium.image.model.RectangleArea;
import com.htmlhifive.pitalium.it.screenshot2.PtlItScreenshotTestBase;
import org.junit.Test;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * iframe内の要素を除外設定とするテスト
 */
public class ExcludeInFrameTest extends PtlItScreenshotTestBase {

	@Test
	public void captureBody_excludeInsideAndOutsideFrame() throws Exception {
		openIFramePage();

		ScreenshotArgument arg = ScreenshotArgument.builder("s").addNewTarget().moveTarget(true).scrollTarget(false)
				.addExcludeByClassName("exclude-target").addExcludeByClassName("content-left")
				.inFrameByClassName("content").build();
		assertionView.assertView(arg);

		// Check
		TargetResult result = loadTargetResults("s").get(0);
		assertThat(result.getExcludes(), hasSize(2));

		// Outside iframe
		Rect rect = getPixelRectBySelector(".exclude-target");
		assertThat(result.getExcludes().get(0).getRectangle(), is(rect.toRectangleArea()));

		// Inside iframe
		PtlWebElement frame = (PtlWebElement) driver.findElementByClassName("content");
		frame.setFrameParent(frame);
		Rect frameRect = getPixelRect(frame);
		Rect targetRect = frame.executeInFrame(new Supplier<Rect>() {
			@Override
			public Rect get() {
				return getPixelRectBySelector(".content-left");
			}
		});

		double x = Math.round(frameRect.x + targetRect.x);
		double y = Math.round(frameRect.y + targetRect.y);
		double width = Math.round(frameRect.width - targetRect.x);
		double height = Math.round(targetRect.height);
		assertThat(result.getExcludes().get(1).getRectangle(), is(new RectangleArea(x, y, width, height)));
	}

	@Test
	public void captureIFrame() throws Exception {
		openIFramePage();

		ScreenshotArgument arg = ScreenshotArgument.builder("s").addNewTargetByClassName("content").moveTarget(true)
				.scrollTarget(true).addExcludeByClassName("content-left").build();
		assertionView.assertView(arg);

		// Check
		TargetResult result = loadTargetResults("s").get(0);
		assertThat(result.getExcludes(), hasSize(1));

		// Inside iframe
		// frame
		PtlWebElement frame = (PtlWebElement) driver.findElementByClassName("content");
		frame.setFrameParent(frame);
		Rect rect = frame.executeInFrame(new Supplier<Rect>() {
			@Override
			public Rect get() {
				return getPixelRectBySelector(".content-left");
			}
		});

		assertThat(result.getExcludes().get(0).getRectangle(), is(rect.round().toRectangleArea()));
	}

	@Test
	public void captureBody_excludeNotExistElementInsideAndOutsideFrame() throws Exception {
		openIFramePage();

		ScreenshotArgument arg = ScreenshotArgument.builder("s").addNewTarget().moveTarget(true).scrollTarget(false)
				.addExcludeByClassName("not-exists").addExcludeByClassName("not-exists").inFrameByClassName("content")
				.build();
		assertionView.assertView(arg);

		// Check
		TargetResult result = loadTargetResults("s").get(0);
		assertThat(result.getExcludes(), is(empty()));
	}

	@Test
	public void captureIFrame_excludeNotExists() throws Exception {
		openIFramePage();

		ScreenshotArgument arg = ScreenshotArgument.builder("s").addNewTargetByClassName("content").moveTarget(true)
				.scrollTarget(true).addExcludeByClassName("not-exists").build();
		assertionView.assertView(arg);

		// Check
		TargetResult result = loadTargetResults("s").get(0);
		assertThat(result.getExcludes(), is(empty()));
	}

}