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

package com.htmlhifive.pitalium.it.assertion.partialPage;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.BrowserType;

import com.htmlhifive.pitalium.core.model.ScreenshotArgument;
import com.htmlhifive.pitalium.it.assertion.PtlItAssertionTestBase;

/**
 * 比較時に要素が存在しない場合のテスト
 */
public class CompareNoSuchElementTest extends PtlItAssertionTestBase {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	/**
	 * 比較時に対象の要素を削除して比較する。
	 * 
	 * @ptl.expect AssertionErrorが発生すること。
	 */
	@Test
	public void compareElementNotExists() throws Exception {
		openBasicTextPage();

		if (isRunTest()) {
			driver.executeJavaScript("" + "var parent = document.getElementById('textRow');"
					+ "var child = document.getElementById('textColumn0');" + "parent.removeChild(child);");
		}

		ScreenshotArgument arg = ScreenshotArgument.builder("s").addNewTargetById("textColumn0").build();

		if (isRunTest()) {
			if (BrowserType.SAFARI.equals(capabilities.getBrowserName())) {
				expectedException.expect(NoSuchElementException.class);
			} else {
				expectedException.expect(AssertionError.class);
			}

			assertionView.assertView(arg);
			fail();
			return;
		}

		assertionView.assertView(arg);
	}

	/**
	 * 複数の要素比較時に一部の要素を削除して比較する。
	 * 
	 * @ptl.expect AssertionErrorが発生すること。
	 */
	@Test
	public void compareMultipleElementsNotExistBySingleSelector() throws Exception {
		openBasicTextPage();

		if (isRunTest()) {
			driver.executeJavaScript("" + "var parent = document.getElementById('textRow');"
					+ "var child = document.getElementById('textColumn0');" + "parent.removeChild(child);");
		}

		ScreenshotArgument arg = ScreenshotArgument.builder("s").addNewTargetByClassName("text-column").build();

		if (isRunTest()) {
			expectedException.expect(AssertionError.class);
			assertionView.assertView(arg);
			fail();
			return;
		}

		assertionView.assertView(arg);
	}

}
