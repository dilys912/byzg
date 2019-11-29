package nc.bs.ic.alert;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import sun.net.TelnetInputStream;
import sun.net.TelnetOutputStream;
import sun.net.ftp.FtpClient;

public class FtpUpfile
{
  private String ip = "";
  private String username = "";
  private String password = "";
  private int port = -1;
  private String path = "";
  FtpClient ftpClient = null;
  OutputStream os = null;
  FileInputStream is = null;
  static String curFolder = System.getProperty("user.dir");

  public FtpUpfile(String serverIP, String username, String password)
  {
    this.ip = serverIP;
    this.username = username;
    this.password = password;
  }

  public FtpUpfile(String serverIP, int port, String username, String password) {
    this.ip = serverIP;
    this.username = username;
    this.password = password;
    this.port = port;
  }

  public void setPath(String path) throws IOException {
    if (this.ftpClient == null)
      this.path = path;
    else
      this.ftpClient.cd(path);
  }

  public boolean connectServer()
  {
    if (this.ftpClient != null) {
      return true;
    }
    this.ftpClient = new FtpClient();
    try {
      if (this.port != -1)
        this.ftpClient.openServer(this.ip, this.port);
      else {
        this.ftpClient.openServer(this.ip);
      }
      this.ftpClient.login(this.username, this.password);
      if (this.path.length() != 0) {
        this.ftpClient.cd(this.path);
      }
      this.ftpClient.binary();
      System.out.println("FTP 已登录到\"" + this.ftpClient.pwd() + "\"目录");

      return true;
    } catch (IOException e) {
      e.printStackTrace();
    }return false;
  }

  public boolean closeServer()
  {
    try
    {
      if (this.is != null) {
        this.is.close();
      }
      if (this.os != null) {
        this.os.close();
      }
      if (this.ftpClient != null) {
        this.ftpClient.closeServer();
      }
      System.out.println("已从服务器断开");

      return true;
    } catch (IOException e) {
      e.printStackTrace();
    }return false;
  }

  public boolean isDirExist(String dir)
  {
    String pwd = "";
    try {
      pwd = this.ftpClient.pwd();
      this.ftpClient.cd(dir);
      this.ftpClient.cd(pwd);
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  public boolean rename(String str1, String str2) throws IOException {
    String file1 = "";
    String file2 = "";
    String folder1 = "";
    String folder2 = "";
    file1 = str1.substring(str1.lastIndexOf("/") + 1, str1.length());
    file2 = str2.substring(str2.lastIndexOf("/") + 1, str2.length());
    folder1 = str1.substring(0, str1.lastIndexOf("/") + 1);
    folder2 = str2.substring(0, str2.lastIndexOf("/") + 1);
    if (!isDirExist(folder2)) {
      createDir(folder2);
    }
    this.ftpClient.rename(str1, str2);
    List l = getFileList(folder2);
    for (int i = 0; i < l.size(); i++) {
      if (l.get(i).toString().indexOf(file2) > 0) {
        return true;
      }
    }
    return false;
  }

  public void sendCMD(String cmd) throws IOException {
    this.ftpClient.sendServer(cmd);

    System.out.println(cmd);
  }

  private boolean createDir(String dir)
  {
    try
    {
      this.ftpClient.ascii();
      StringTokenizer s = new StringTokenizer(dir, "/");
      s.countTokens();
      String pathName = "";
      while (s.hasMoreElements()) {
        pathName = pathName + "/" + (String)s.nextElement();
        if (!isDirExist(pathName))
        {
          try
          {
            this.ftpClient.sendServer("MKD " + pathName + "\r\n");
          } catch (Exception e) {
            e = null;
            return false;
          }
          this.ftpClient.readServerResponse();
        }
      }
      this.ftpClient.binary();
      return true;
    } catch (IOException e1) {
      e1.printStackTrace();
    }return false;
  }

  public boolean upload(String filename)
  {
    String newname = "";
    if (filename.indexOf("/") > -1)
      newname = filename.substring(filename.lastIndexOf("/") + 1);
    else {
      newname = filename;
    }
    return upload(filename, newname);
  }

  public boolean upload(String fileName, String newName)
  {
    try
    {
      String savefilename = new String(fileName.getBytes("ISO-8859-1"), "GBK");
      File file_in = new File(savefilename);
      if (!file_in.exists()) {
        throw new Exception("此文件或文件夹[" + file_in.getName() + "]有误或不存在!");
      }
      if (file_in.isDirectory())
        upload(file_in.getPath(), newName, this.ftpClient.pwd());
      else {
        uploadFile(file_in.getPath(), newName);
      }

      if (this.is != null) {
        this.is.close();
      }
      if (this.os != null) {
        this.os.close();
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Exception e in Ftp upload(): " + e.toString());
      return false;
    } finally {
      try {
        if (this.is != null) {
          this.is.close();
        }
        if (this.os != null)
          this.os.close();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void upload(String fileName, String newName, String path)
    throws Exception
  {
    String savefilename = new String(fileName.getBytes("ISO-8859-1"), "GBK");
    File file_in = new File(savefilename);
    if (!file_in.exists()) {
      throw new Exception("此文件或文件夹[" + file_in.getName() + "]有误或不存在!");
    }
    if (file_in.isDirectory()) {
      if (!isDirExist(newName)) {
        createDir(newName);
      }
      this.ftpClient.cd(newName);
      File[] sourceFile = file_in.listFiles();
      for (int i = 0; i < sourceFile.length; i++)
        if (sourceFile[i].exists())
        {
          if (sourceFile[i].isDirectory())
            upload(sourceFile[i].getPath(), sourceFile[i].getName(), path + "/" + newName);
          else
            uploadFile(sourceFile[i].getPath(), sourceFile[i].getName());
        }
    }
    else {
      uploadFile(file_in.getPath(), newName);
    }
    this.ftpClient.cd(path);
  }

  public long uploadFile(String filename, String newname)
    throws Exception
  {
    long result = 0L;
    TelnetOutputStream os = null;
    FileInputStream is = null;
    try {
      File file_in = new File(filename);
      if (!file_in.exists())
        return -1L;
      //File file_in;
      os = this.ftpClient.put(newname);
      result = file_in.length();
      is = new FileInputStream(file_in);
      byte[] bytes = new byte[1024];
      int c;
      while ((c = is.read(bytes)) != -1)
      {
        //int c;
        os.write(bytes, 0, c);
      }
    } finally {
      if (is != null) {
        is.close();
      }
      if (os != null)
        os.close();
    }
    if (is != null) {
      is.close();
    }
    if (os != null) {
      os.close();
    }

    return result;
  }

  public boolean downloadFile(String filename, String newfilename)
  {
    boolean result = false;
    TelnetInputStream is = null;
    FileOutputStream os = null;
    try {
      is = this.ftpClient.get(filename);
      File outfile = new File(newfilename);
      os = new FileOutputStream(outfile);
      byte[] bytes = new byte[1024];
      int c;
      while ((c = is.read(bytes)) != -1)
      {
        //int c;
        os.write(bytes, 0, c);
        result = true;
      }
    } catch (IOException e) {
      e.printStackTrace();
      try
      {
        if (is != null) {
          is.close();
        }
        if (os != null)
          os.close();
      }
      catch (IOException e1) {
        e1.printStackTrace();
      }
    }
    finally
    {
      try
      {
        if (is != null) {
          is.close();
        }
        if (os != null)
          os.close();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public List getFileList(String path)
  {
    List list = new ArrayList();
    List list1 = new ArrayList();
    try
    {
      System.out.println(this.path + path + "temp");
      DataInputStream dis = new DataInputStream(this.ftpClient.nameList(this.path + path));
      String filename = "";

      while ((filename = dis.readLine()) != null) {
        String sfilename = new String(filename.getBytes("ISO-8859-1"), "utf-8");
        list.add(sfilename);
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return list;
  }

  public static void main(String[] args) throws IOException {
    String tempFolder = "tmp";
    File file = new File(curFolder + "/" + tempFolder);
    if (!file.exists()) {
      file.mkdir();
    }
    FtpUpfile ftp = new FtpUpfile("100.100.100.100", 21, "wmsadm", "abc123456");
    ftp.setPath("/sapinstall/WMIMM");
    ftp.connectServer();

    List list = ftp.getFileList("/PIX");
    for (int i = 0; i < list.size(); i++) {
      String name = new String(list.get(i).toString().getBytes("UTF-8"), "iso-8859-1");
      System.out.println(name);
      if (!ftp.isDirExist(name)) {
        ftp.downloadFile(name, curFolder + "\\" + tempFolder + "\\" + name.substring(name.lastIndexOf("/") + 1, name.length()));
      }
    }
    ftp.closeServer();
  }
}