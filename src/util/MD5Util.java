package util;
import java.io.File;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

public class MD5Util {
	public static String getFileMD5String(File f) throws Exception {
		return DigestUtils.md5Hex(FileUtils.readFileToByteArray(f));
	}

}
