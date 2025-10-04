package nccc.btp.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import nccc.btp.vo.FileVo;

public class FileUtil {

  private FileUtil() {

  }

  // 壓縮成zip
  public static byte[] compressToZip(List<MultipartFile> files) throws IOException {
    if (files == null || files.isEmpty()) {
      return new byte[0];
    }
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos)) {

      for (MultipartFile multipart : files) {
        if (multipart == null || multipart.isEmpty()) {
          continue;
        }
        String originalFilename = multipart.getOriginalFilename();
        if (originalFilename == null) {
          // 如果沒有檔名，跳過
          continue;
        }
        ZipEntry entry = new ZipEntry(originalFilename);
        zos.putNextEntry(entry);

        try (InputStream is = multipart.getInputStream()) {
          byte[] buffer = new byte[8192];
          int len;
          while ((len = is.read(buffer)) != -1) {
            zos.write(buffer, 0, len);
          }
        }
        zos.closeEntry();
      }
      zos.finish();
      return baos.toByteArray();
    }
  }

  // 從zipBytes 取出所有檔名
  public static List<String> listFileNames(byte[] zipBytes) throws IOException {
    List<String> names = new ArrayList<>();
    if (zipBytes == null || zipBytes.length == 0) {
      return names;
    }
    try (ByteArrayInputStream bais = new ByteArrayInputStream(zipBytes);
        ZipInputStream zis = new ZipInputStream(bais)) {

      ZipEntry entry;
      while ((entry = zis.getNextEntry()) != null) {
        if (!entry.isDirectory()) {
          names.add(entry.getName());
        }
        zis.closeEntry();
      }
    }
    return names;
  }

  // 從zip裡取得全部檔案
  public static List<FileVo> getAllFiles(byte[] zipBytes) throws IOException {
    List<FileVo> files = new ArrayList<>();
    if (zipBytes == null || zipBytes.length == 0) {
      return files;
    }

    try (ByteArrayInputStream bais = new ByteArrayInputStream(zipBytes);
        ZipInputStream zis = new ZipInputStream(bais)) {
      ZipEntry entry;
      while ((entry = zis.getNextEntry()) != null) {
        if (!entry.isDirectory()) {
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          byte[] buffer = new byte[8192];
          int len;
          while ((len = zis.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
          }

          String base64 = Base64.getEncoder().encodeToString(baos.toByteArray());

          FileVo fileVo = new FileVo();
          fileVo.setFileName(entry.getName());
          fileVo.setBase64(base64);
          files.add(fileVo);
        }
        zis.closeEntry();
      }
    }

    return files;
  }

  /**
   * 驗證並清理檔案路徑字串，移除潛在的危險字符和路徑遍歷序列。
   * 
   * @param filePath 原始檔案路徑字串
   * @return 清理後的檔案路徑字串
   */
  public static String validFilePath(String filePath) {
    if (StringUtils.isBlank(filePath)) {
      return "";
    }

    // 步驟 1: 正規化路徑分隔符，統一為 '/'
    String cleanedPath = filePath.replace("\\", "/");

    // 步驟 2: 移除路徑遍歷序列 (例如: "../", "..\" )
    cleanedPath = cleanedPath.replace("../", "").replace("..\\", "").replace("..", ""); // 針對根目錄下的
                                                                                        // ".."

    // 步驟 3: 移除常見的無效檔案名字符
    cleanedPath = cleanedPath.replaceAll("[<>:\"|?*]", ""); // 移除常用於 Windows 的無效字符

    int lastSlashIndex = cleanedPath.lastIndexOf("/");
    if (lastSlashIndex != -1) {
      cleanedPath = cleanedPath.substring(lastSlashIndex + 1);
    }

    // 步驟 4: 修剪空白字符
    cleanedPath = cleanedPath.trim();

    return cleanedPath;
  }


}
