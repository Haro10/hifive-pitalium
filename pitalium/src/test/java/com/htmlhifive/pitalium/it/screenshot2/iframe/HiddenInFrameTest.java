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

import com.htmlhifive.pitalium.core.model.ScreenshotArgument;
import com.htmlhifive.pitalium.it.screenshot2.PtlItScreenshotTestBase;
import org.junit.Test;

import java.awt.image.BufferedImage;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * iframe内の要素を非表示設定とするテスト
 */
public class HiddenInFrameTest extends PtlItScreenshotTestBase {

	/**
	 * BODYを撮影する際に、IFRAME内の要素を非表示に設定する。
	 * 
	 * @ptl.expect 非表示に設定した要素が写っていないこと。
	 */
	@Test
	public void captureBody() throws Exception {
		openIFramePage();

		ScreenshotArgument arg = ScreenshotArgument.builder("s").addNewTarget().moveTarget(true).scrollTarget(false)
				.addHiddenElementsByClassName("content-left").inFrameByClassName("content").build();
		assertionView.assertView(arg);

		// Check
		// 指定した色のピクセルが存在しないこと
		BufferedImage image = loadTargetResults("s").get(0).getImage().get();
		int width = image.getWidth();
		int height = image.getHeight();
		Color hiddenColor = Color.valueOf("#795548");
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Color actual = Color.valueOf(image.getRGB(x, y));
				assertThat(actual, is(not(hiddenColor)));
			}
		}
	}

	/**
	 * IFRAMEを撮影する際に、IFRAME内の要素を非表示に設定する。
	 * 
	 * @ptl.expect 非表示に設定した要素が写っていないこと。
	 */
	@Test
	public void captureIFrame() throws Exception {
		openIFramePage();

		ScreenshotArgument arg = ScreenshotArgument.builder("s").addNewTargetByClassName("content").moveTarget(true)
				.scrollTarget(true).addHiddenElementsByClassName("content-left").inFrameByClassName("content").build();
		assertionView.assertView(arg);

		// Check
		// 指定した色のピクセルが存在しないこと
		BufferedImage image = loadTargetResults("s").get(0).getImage().get();
		int width = image.getWidth();
		int height = image.getHeight();
		Color hiddenColor = Color.valueOf("#795548");
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Color actual = Color.valueOf(image.getRGB(x, y));
				assertThat(actual, is(not(hiddenColor)));
			}
		}
	}

	/**
	 * BODYを撮影する際に、IFRAME内の存在しない要素を非表示に設定する。
	 * 
	 * @ptl.expect エラーが発生しないこと。
	 */
	@Test
	public void captureBody_hiddenNotExistElement() throws Exception {
		openIFramePage();

		ScreenshotArgument arg = ScreenshotArgument.builder("s").addNewTarget().moveTarget(true).scrollTarget(false)
				.addHiddenElementsByClassName("not-exists").inFrameByClassName("content").build();
		assertionView.assertView(arg);

		// エラーにならない
		assertTrue(true);
	}

	/**
	 * IFRAMEを撮影する際に、IFRAME内の存在しない要素を非表示に設定する。
	 * 
	 * @ptl.expect エラーが発生しないこと。
	 */
	@Test
	public void captureIFrame_hiddenNotExistElement() throws Exception {
		openIFramePage();

		ScreenshotArgument arg = ScreenshotArgument.builder("s").addNewTargetByClassName("content").moveTarget(true)
				.scrollTarget(true).addHiddenElementsByClassName("not-exists").inFrameByClassName("content").build();
		assertionView.assertView(arg);

		// エラーにならない
		assertTrue(true);
	}

}
