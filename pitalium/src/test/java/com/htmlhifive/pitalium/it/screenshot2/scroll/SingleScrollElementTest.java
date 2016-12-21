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

package com.htmlhifive.pitalium.it.screenshot2.scroll;

import com.htmlhifive.pitalium.core.model.ScreenshotArgument;
import com.htmlhifive.pitalium.it.RequireVisualCheck;
import com.htmlhifive.pitalium.it.screenshot2.PtlItScreenshotTestBase;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.util.Map;

import static com.htmlhifive.pitalium.it.PtlItTestBase.IsGradation.gradationWithBorder;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * ページの特定要素のスクリーンショットが正しくとれているかのテスト
 */
public class SingleScrollElementTest extends PtlItScreenshotTestBase {

	@Test
	public void takeDivScreenshot_scroll_move() throws Exception {
		openScrollPage();

		ScreenshotArgument arg = ScreenshotArgument.builder("s").addNewTargetById("div-scroll").scrollTarget(true)
				.moveTarget(true).build();
		assertionView.assertView(arg);

		// Check
		double ratio = getPixelRatio();
		Rect rect = getPixelRectBySelector("#div-scroll > #div-scroll-inner");
		// IEはborderWidthが空文字なのでborderLeftWidthを見る
		double border = driver.<Number> executeJavaScript(
				"" + "var el = document.getElementById('div-scroll');" + "var style = window.getComputedStyle(el);"
						+ "var borderWidth = parseFloat(style.borderWidth || style.borderLeftWidth);"
						+ "return borderWidth;").doubleValue();
		BufferedImage image = loadTargetResults("s").get(0).getImage().get();
		assertThat(image.getHeight(), is((int) (rect.round().height + Math.round(border * ratio) * 2)));
		assertThat(image, is(gradationWithBorder(ratio, Color.BLACK, 1)));
	}

	@Test
	@RequireVisualCheck
	public void takeTextareaScreenshot_scroll_move() throws Exception {
		openScrollPage();

		ScreenshotArgument arg = ScreenshotArgument.builder("s").addNewTargetById("textarea-scroll").scrollTarget(true)
				.moveTarget(true).build();
		assertionView.assertView(arg);

		// Check
		Rect rect = getPixelRectById("textarea-scroll");
		BufferedImage image = loadTargetResults("s").get(0).getImage().get();
		assertThat(image.getHeight(), is(greaterThan((int) rect.height)));
	}

	@Test
	public void takeTableScreenshot_scroll_move() throws Exception {
		openScrollPage();

		ScreenshotArgument arg = ScreenshotArgument.builder("s").addNewTargetByCssSelector("#table-scroll > tbody")
				.scrollTarget(true).moveTarget(true).build();
		assertionView.assertView(arg);

		// Check
		double ratio = getPixelRatio();
		Rect rect = getPixelRectById("table-scroll");
		BufferedImage image = loadTargetResults("s").get(0).getImage().get();
		assertThat(image.getHeight(), is(greaterThan((int) rect.height)));

		int x = image.getWidth() / 2;
		int y = 0;
		int cellCount = 0;
		while (cellCount * 16 <= 240) {
			int color = cellCount * 16;
			Color expect = Color.rgb(color, color, color);
			cellCount++;
			int maxY = (int) Math.round(cellCount * 20 * ratio);
			while (y < maxY) {
				Color actual = Color.valueOf(image.getRGB(x, y));
				assertThat(String.format("Point (%d, %d) is not match.", x, y), actual, is(expect));
				y++;
			}
		}
	}

	@Test
	public void takeIFrameScreenshot_scroll_move() throws Exception {
		openScrollPage();

		ScreenshotArgument arg = ScreenshotArgument.builder("s").addNewTargetByName("iframe-scroll").scrollTarget(true)
				.moveTarget(true).build();
		assertionView.assertView(arg);

		// Check
		double ratio = getPixelRatio();
		Rect rect = getPixelRect(driver.findElementByName("iframe-scroll"));
		BufferedImage image = loadTargetResults("s").get(0).getImage().get();
		assertThat(image.getHeight(), is(greaterThan((int) rect.height)));

		// 2pxボーダーが写るので無視する
		int border = (int) Math.round(ratio * 2);
		image = image.getSubimage(border, border, image.getWidth() - border * 2, image.getHeight() - border * 2);
		assertThat(image, is(gradationWithBorder(ratio)));
	}

	@Test
	public void takeDivScreenshot_scroll_notMove() throws Exception {
		openScrollPage();

		ScreenshotArgument arg = ScreenshotArgument.builder("s").addNewTargetById("div-scroll").scrollTarget(true)
				.moveTarget(false).build();
		assertionView.assertView(arg);

		// Check
		double ratio = getPixelRatio();
		Rect rect = getPixelRectBySelector("#div-scroll > #div-scroll-inner");
		// IEはborderWidthが空文字なのでborderLeftWidthを見る
		double border = driver.<Number> executeJavaScript(
				"" + "var el = document.getElementById('div-scroll');" + "var style = window.getComputedStyle(el);"
						+ "var borderWidth = parseFloat(style.borderWidth || style.borderLeftWidth);"
						+ "return borderWidth;").doubleValue();
		BufferedImage image = loadTargetResults("s").get(0).getImage().get();
		assertThat(image.getHeight(), is((int) (rect.round().height + Math.round(border * ratio) * 2)));
		assertThat(image, is(gradationWithBorder(ratio, Color.BLACK, 1)));
	}

	@Test
	@RequireVisualCheck
	public void takeTextareaScreenshot_scroll_notMove() throws Exception {
		openScrollPage();

		ScreenshotArgument arg = ScreenshotArgument.builder("s").addNewTargetById("textarea-scroll").scrollTarget(true)
				.moveTarget(false).build();
		assertionView.assertView(arg);

		// Check
		Rect rect = getPixelRectById("textarea-scroll");
		BufferedImage image = loadTargetResults("s").get(0).getImage().get();
		assertThat(image.getHeight(), is(greaterThan((int) rect.height)));
	}

	@Test
	public void takeTableScreenshot_scroll_notMove() throws Exception {
		openScrollPage();

		ScreenshotArgument arg = ScreenshotArgument.builder("s").addNewTargetByCssSelector("#table-scroll > tbody")
				.scrollTarget(true).moveTarget(false).build();
		assertionView.assertView(arg);

		// Check
		double ratio = getPixelRatio();
		Rect rect = getPixelRectById("table-scroll");
		BufferedImage image = loadTargetResults("s").get(0).getImage().get();
		assertThat(image.getHeight(), is(greaterThan((int) rect.height)));

		int x = image.getWidth() / 2;
		int y = 0;
		int cellCount = 0;
		while (cellCount * 16 <= 240) {
			int color = cellCount * 16;
			Color expect = Color.rgb(color, color, color);
			cellCount++;
			int maxY = (int) Math.round(cellCount * 20 * ratio);
			while (y < maxY) {
				Color actual = Color.valueOf(image.getRGB(x, y));
				assertThat(String.format("Point (%d, %d) is not match.", x, y), actual, is(expect));
				y++;
			}
		}
	}

	@Test
	public void takeIFrameScreenshot_scroll_notMove() throws Exception {
		openScrollPage();

		ScreenshotArgument arg = ScreenshotArgument.builder("s").addNewTargetByName("iframe-scroll").scrollTarget(true)
				.moveTarget(false).build();
		assertionView.assertView(arg);

		// Check
		double ratio = getPixelRatio();
		Rect rect = getPixelRect(driver.findElementByName("iframe-scroll"));
		BufferedImage image = loadTargetResults("s").get(0).getImage().get();
		assertThat(image.getHeight(), is(greaterThan((int) rect.height)));

		// 1pxボーダーが写るので無視する
		int border = (int) Math.round(ratio * 2);
		image = image.getSubimage(border, border, image.getWidth() - border * 2, image.getHeight() - border * 2);
		assertThat(image, is(gradationWithBorder(ratio)));
	}

	@Test
	public void takeDivScreenshot_notScroll_move() throws Exception {
		openScrollPage();

		ScreenshotArgument arg = ScreenshotArgument.builder("s").addNewTargetById("div-scroll").scrollTarget(false)
				.moveTarget(true).build();
		assertionView.assertView(arg);

		// Check
		Rect rect = getPixelRectById("div-scroll");
		BufferedImage image = loadTargetResults("s").get(0).getImage().get();
		assertThat(image.getHeight(), is((int) rect.round().height));

		// スクロールバーの検証が出来ないため、大きさだけチェックする
	}

	@Test
	@RequireVisualCheck
	public void takeTextareaScreenshot_notScroll_move() throws Exception {
		openScrollPage();

		ScreenshotArgument arg = ScreenshotArgument.builder("s").addNewTargetById("textarea-scroll")
				.scrollTarget(false).moveTarget(true).build();
		assertionView.assertView(arg);

		// Check
		Rect rect = getPixelRectById("textarea-scroll");
		BufferedImage image = loadTargetResults("s").get(0).getImage().get();
		assertThat(image.getHeight(), is((int) rect.round().height));
	}

	@Test
	public void takeTableScreenshot_notScroll_move() throws Exception {
		openScrollPage();

		ScreenshotArgument arg = ScreenshotArgument.builder("s").addNewTargetByCssSelector("#table-scroll > tbody")
				.scrollTarget(false).moveTarget(true).build();
		assertionView.assertView(arg);

		// Check
		double ratio = getPixelRatio();
		Rect rect = getPixelRectById("table-scroll");
		BufferedImage image = loadTargetResults("s").get(0).getImage().get();
		assertThat(image.getHeight(), is((int) rect.round().height));

		int x = image.getWidth() / 2;
		int y = 0;
		int cellCount = 0;
		while (cellCount * 16 <= 240 && y < image.getHeight()) {
			int color = cellCount * 16;
			Color expect = Color.rgb(color, color, color);
			cellCount++;
			int maxY = (int) Math.round(cellCount * 20 * ratio);
			while (y < maxY && y < image.getHeight()) {
				Color actual = Color.valueOf(image.getRGB(x, y));
				assertThat(expect, is(actual));
				y++;
			}
		}
	}

	@Test
	public void takeIFrameScreenshot_notScroll_move() throws Exception {
		openScrollPage();

		ScreenshotArgument arg = ScreenshotArgument.builder("s").addNewTargetByName("iframe-scroll")
				.scrollTarget(false).moveTarget(true).build();
		assertionView.assertView(arg);

		// Check
		Rect rect = getPixelRect(driver.findElementByName("iframe-scroll"));
		BufferedImage image = loadTargetResults("s").get(0).getImage().get();
		assertThat(image.getHeight(), is((int) rect.round().height));

		// スクロールバーの検証が出来ないため、大きさだけチェックする
	}

	@Test
	public void takeDivScreenshot_notScroll_notMove() throws Exception {
		openScrollPage();

		ScreenshotArgument arg = ScreenshotArgument.builder("s").addNewTargetById("div-scroll").scrollTarget(false)
				.moveTarget(false).build();
		assertionView.assertView(arg);

		// Check
		Rect rect = getPixelRectById("div-scroll");
		BufferedImage image = loadTargetResults("s").get(0).getImage().get();
		assertThat(image.getHeight(), is((int) rect.round().height));

		// スクロールバーの検証が出来ないため、大きさだけチェックする
	}

	@Test
	@RequireVisualCheck
	public void takeTextareaScreenshot_notScroll_notMove() throws Exception {
		openScrollPage();

		ScreenshotArgument arg = ScreenshotArgument.builder("s").addNewTargetById("textarea-scroll")
				.scrollTarget(false).moveTarget(false).build();
		assertionView.assertView(arg);

		// Check
		Rect rect = getPixelRectById("textarea-scroll");
		BufferedImage image = loadTargetResults("s").get(0).getImage().get();
		assertThat(image.getHeight(), is((int) rect.round().height));
	}

	@Test
	public void takeTableScreenshot_notScroll_notMove() throws Exception {
		openScrollPage();

		ScreenshotArgument arg = ScreenshotArgument.builder("s").addNewTargetByCssSelector("#table-scroll > tbody")
				.scrollTarget(false).moveTarget(false).build();
		assertionView.assertView(arg);

		// Check
		double ratio = getPixelRatio();
		Rect rect = getPixelRectById("table-scroll");
		BufferedImage image = loadTargetResults("s").get(0).getImage().get();
		assertThat(image.getHeight(), is((int) rect.round().height));

		int x = image.getWidth() / 2;
		int y = 0;
		int cellCount = 0;
		while (cellCount * 16 <= 240 && y < image.getHeight()) {
			int color = cellCount * 16;
			Color expect = Color.rgb(color, color, color);
			cellCount++;
			int maxY = (int) Math.round(cellCount * 20 * ratio);
			while (y < maxY && y < image.getHeight()) {
				Color actual = Color.valueOf(image.getRGB(x, y));
				assertThat(expect, is(actual));
				y++;
			}
		}
	}

	@Test
	public void takeIFrameScreenshot_notScroll_notMove() throws Exception {
		openScrollPage();

		ScreenshotArgument arg = ScreenshotArgument.builder("s").addNewTargetByName("iframe-scroll")
				.scrollTarget(false).moveTarget(false).build();
		assertionView.assertView(arg);

		// Check
		Rect rect = getPixelRect(driver.findElementByName("iframe-scroll"));
		BufferedImage image = loadTargetResults("s").get(0).getImage().get();
		assertThat(image.getHeight(), is((int) rect.round().height));

		// スクロールバーの検証が出来ないため、大きさだけチェックする
	}

}