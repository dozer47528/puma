package com.dianping.puma.core.util;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

public class ScheduledExecutorUtils {

	private static final String PREFIX_FACTORY = "threadfactory-";

	private static final String INFIX_MARK = "-";

	private static final String PREFIX_POOLSIZE = "poolsize-";

	private static Map<String, WeakReference<ScheduledExecutorService>> scheduledExecutorServices = new ConcurrentHashMap<String, WeakReference<ScheduledExecutorService>>();

	public static ScheduledExecutorService createScheduledExecutorService(int poolSize, String factoryName) {
		if (scheduledExecutorServices.containsKey(factoryName)
				&& scheduledExecutorServices.get(factoryName).get() != null) {
			return scheduledExecutorServices.get(factoryName).get();
		}
		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(poolSize, new PumaThreadFactory(
				PREFIX_FACTORY + factoryName + INFIX_MARK + PREFIX_POOLSIZE + Integer.toString(poolSize) + INFIX_MARK));
		scheduledExecutorServices.put(factoryName, new WeakReference<ScheduledExecutorService>(executorService));
		return executorService;
	}

	public static ScheduledExecutorService createSingleScheduledExecutorService(String factoryName) {
		if (scheduledExecutorServices.containsKey(factoryName)
				&& scheduledExecutorServices.get(factoryName).get() != null) {
			return scheduledExecutorServices.get(factoryName).get();
		}
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(new PumaThreadFactory(
				PREFIX_FACTORY + factoryName + INFIX_MARK + PREFIX_POOLSIZE +  Integer.toString(1) + INFIX_MARK));
		scheduledExecutorServices.put(factoryName, new WeakReference<ScheduledExecutorService>(executorService));
		return executorService;
	}

	private static class PumaThreadFactory implements ThreadFactory {

		private String factoryName;

		public PumaThreadFactory(String factoryName) {
			this.factoryName = factoryName;
		}

		@Override
		public Thread newThread(Runnable r) {
			return PumaThreadUtils.createThread(r, factoryName, false);
		}
	}

}