package com.dianping.puma.core.storage.backup;

import com.dianping.puma.core.exception.BackupException;
import com.dianping.puma.core.storage.backup.strategy.AlwaysDeleteStrategy;
import com.dianping.puma.core.storage.backup.strategy.DeleteStrategy;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LocalFileBasedBackupTest {

	LocalFileBasedBackup localFileBasedBackup = new LocalFileBasedBackup();

	@Before
	public void init() {
		localFileBasedBackup.setWorkingBaseFolder("/data/appdatas/puma-test/binlog/");
		localFileBasedBackup.setBackupBaseFolder("/data/appdatas/puma-test/bak/binlog");
		localFileBasedBackup.setBackupDay(1);
	}

	@Test
	public void deleteFolderTest() throws NoSuchMethodException, IOException, IllegalAccessException,
			InvocationTargetException {

		String folderName = "/data/appdatas/puma-test/folder/";

		File folder = new File(folderName);

		if (folder.mkdirs()) {

			Assert.assertTrue(folder.exists());

			Method deleteFolder = LocalFileBasedBackup.class
					.getDeclaredMethod("deleteFolder", File.class, DeleteStrategy.class);
			deleteFolder.setAccessible(true);
			deleteFolder.invoke(localFileBasedBackup, folder, new AlwaysDeleteStrategy());
		}

		Assert.assertFalse(folder.exists());
	}

	@Test
	public void DeleteTest() throws BackupException {
		localFileBasedBackup.delete();
	}
}