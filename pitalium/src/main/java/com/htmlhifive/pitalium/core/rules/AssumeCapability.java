package com.htmlhifive.pitalium.core.rules;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.junit.AssumptionViolatedException;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.htmlhifive.pitalium.core.annotation.CapabilityFilter;
import com.htmlhifive.pitalium.core.annotation.CapabilityFilters;

/**
 * {@link CapabilityFilter}および{@link CapabilityFilters}でテスト実行を制限します。
 */
public class AssumeCapability extends TestWatcher {

	private static final Logger LOG = LoggerFactory.getLogger(AssumeCapability.class);

	private Description description;

	@Override
	protected void starting(Description description) {
		super.starting(description);
		this.description = description;
	}

	public void setDescription(Description description) {
		this.description = description;
	}

	public void assumeCapability(final Capabilities capabilities) {
		LOG.debug("[assumeCapability start]");
		if (description == null) {
			LOG.warn("[AssumeCapability] Description is empty. assumeCapability should be called after test starting.");
			return;
		}

		// ClassのAnnotation取得
		// MEMO: LoadingCacheを利用してClass、Annotation単位でFilter情報をキャッシュしても
		//       参照頻度が多くないため逆に遅くなる
		Collection<CapabilityFilter> classFilters = getFilters(
				Arrays.asList(description.getTestClass().getAnnotations()));
		if (!classFilters.isEmpty()) {
			boolean classResult = FluentIterable.from(classFilters).anyMatch(new Predicate<CapabilityFilter>() {
				@Override
				public boolean apply(CapabilityFilter f) {
					return isMatch(f, capabilities);
				}
			});
			if (!classResult) {
				LOG.debug("[Testcase assumed] (name: {})", description.getDisplayName());
				throw new AssumptionViolatedException("AssumeCapability");
			}
		}

		Collection<CapabilityFilter> methodFilters = getFilters(description.getAnnotations());
		if (methodFilters.isEmpty()) {
			return;
		}

		boolean methodResult = FluentIterable.from(methodFilters).anyMatch(new Predicate<CapabilityFilter>() {
			@Override
			public boolean apply(CapabilityFilter f) {
				return isMatch(f, capabilities);
			}
		});
		if (!methodResult) {
			LOG.debug("[Testcase assumed] (name: {})", description.getDisplayName());
			throw new AssumptionViolatedException("AssumeCapability");
		}
		LOG.debug("[assumeCapability finished]");
	}

	/**
	 * アノテーションから{@link CapabilityFilter}一覧を取得します。
	 * 
	 * @param annotations アノテーション
	 * @return アノテーションに含まれるCapabilityFilter一覧
	 */
	private static Collection<CapabilityFilter> getFilters(Iterable<Annotation> annotations) {
		List<CapabilityFilter> filters = new ArrayList<>();
		for (Annotation annotation : annotations) {
			if (annotation instanceof CapabilityFilter) {
				filters.add((CapabilityFilter) annotation);
				continue;
			}
			if (annotation instanceof CapabilityFilters) {
				Collections.addAll(filters, ((CapabilityFilters) annotation).value());
				continue;
			}

			for (Annotation a : annotation.annotationType().getDeclaredAnnotations()) {
				if (a instanceof CapabilityFilter) {
					filters.add((CapabilityFilter) a);
					continue;
				}
				if (a instanceof CapabilityFilters) {
					Collections.addAll(filters, ((CapabilityFilters) a).value());
				}
			}
		}
		return filters;
	}

	/**
	 * CapabilitiesをCapabilityFilterでテストします。
	 * 
	 * @param filter テスト用のCapabilityFilter
	 * @param capabilities テスト対象のCapabilities
	 * @return テストに通過した場合true、通過しない場合false
	 */
	private static boolean isMatch(CapabilityFilter filter, Capabilities capabilities) {
		// FIXME: Java8対応はよ！！
		if (filter.version().length > 0) {
			final String version = capabilities.getVersion();
			boolean result = FluentIterable.from(filter.version()).anyMatch(new Predicate<String>() {
				@Override
				public boolean apply(String s) {
					return Strings.isNullOrEmpty(s) ? Strings.isNullOrEmpty(version) : version.matches(s);
				}
			});
			if (!result) {
				return false;
			}
		}

		if (filter.platform().length > 0) {
			final Collection<Platform> platforms = toPlatformFamily(capabilities.getPlatform());
			boolean result = FluentIterable.from(filter.platform()).anyMatch(new Predicate<Platform>() {
				@Override
				public boolean apply(Platform p) {
					return p == Platform.ANY || platforms.contains(p);
				}
			});
			if (!result) {
				return false;
			}
		}

		if (filter.browserName().length > 0) {
			final String browserName = capabilities.getBrowserName();
			boolean result = FluentIterable.from(filter.browserName()).anyMatch(new Predicate<String>() {
				@Override
				public boolean apply(String s) {
					return Strings.isNullOrEmpty(s) ? Strings.isNullOrEmpty(browserName) : s.equals(browserName);
				}
			});
			if (!result) {
				return false;
			}
		}

		if (filter.deviceName().length > 0) {
			final String deviceName = Strings.nullToEmpty((String) capabilities.getCapability("deviceName"));
			boolean result = FluentIterable.from(filter.deviceName()).anyMatch(new Predicate<String>() {
				@Override
				public boolean apply(String s) {
					return Strings.isNullOrEmpty(s) ? Strings.isNullOrEmpty(deviceName) : deviceName.matches(s);
				}
			});
			if (!result) {
				return false;
			}
		}

		if (filter.filterGroup().length == 0) {
			return true;
		}

		final String filterGroup = Strings.nullToEmpty((String) capabilities.getCapability("filterGroup"));
		return FluentIterable.from(filter.filterGroup()).anyMatch(new Predicate<String>() {
			@Override
			public boolean apply(String s) {
				return Strings.isNullOrEmpty(s) ? Strings.isNullOrEmpty(filterGroup) : s.equals(filterGroup);
			}
		});
	}

	/**
	 * {@link Platform#family()}から辿れるPlatformの親一覧を取得します。
	 * 
	 * @param platform 対象のPlatform
	 * @return 引数のPlatformと、引数のPlatformの親Platform一覧
	 */
	private static Collection<Platform> toPlatformFamily(Platform platform) {
		Set<Platform> platforms = EnumSet.noneOf(Platform.class);
		if (platform == null) {
			return platforms;
		}

		platforms.add(platform);

		Platform parent = platform.family();
		while (parent != null && parent != Platform.ANY) {
			platforms.add(parent);
			parent = parent.family();
		}
		return platforms;
	}

}
