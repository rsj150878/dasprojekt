package com.app.dbIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.app.enumPackage.DokumentGehoertZuType;
import com.app.filestorage.HundeDokumente;

public class DBHundDokumente {


	public HundeDokumente getHundDokumentForId(DokumentGehoertZuType type, Integer gehoertZu) throws Exception {
		
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();

		PreparedStatement st = conn.prepareStatement("select * from files where gehoertZu = ? and gehoertZuType = ?");
		st.setInt(1, gehoertZu);
		st.setInt(2,type.getGehoertZu());
		
		ResultSet rs = st.executeQuery();
		
		HundeDokumente result = null;
		if (rs.next()) {
			result = new HundeDokumente();
			result.setIdFile(rs.getInt("idfiles"));
			result.setGehoertZu(rs.getInt("gehoertZu"));
			result.setFileName(rs.getString("filename"));
			result.setGehoertZuType(rs.getInt("gehoertZuType"));
			
			Blob blob = rs.getBlob("fileContent");
			InputStream in = blob.getBinaryStream();
			File zwFile = new File(result.getFileName());
			OutputStream out = new FileOutputStream(zwFile);
			
			byte[] buff = blob.getBytes(1, (int) blob.length());
//			byte[] buff = new byte[1];
//			while (in.read(buff) > 0) {
//				out.write(buff);
//			}
			out.write(buff);
			out.close();
			result.setHundDokument(zwFile);
			
			//FileInputStream fis1= new FileInputStream(zwFile);
			//FileInputStream fis2 = new FileInputStream(new File("files/Urkunde allgemein_FORMULAR1.pdf"));
			
		}
		
		return result;
		
	}
	
	public void saveDokument(HundeDokumente saveDokument) throws Exception {

		Connection conn = DBConnectionNeu.INSTANCE.getConnection();

		PreparedStatement st = conn.prepareStatement("select * from files where gehoertZu = ?");
		st.setInt(1, saveDokument.getGehoertZu());
		
		ResultSet rs = st.executeQuery();
		if (rs.next()) {
			
		} else {
			File saveFile = saveDokument.getHundDokument();
			FileInputStream is = new FileInputStream(saveFile);
			
			PreparedStatement ins = conn.prepareStatement("insert into files (filename, gehoertZu,gehoertZuType, fileContent)" +
			" values (?,?,?,?)");
			
			ins.setString(1, saveDokument.getFileName());
			ins.setInt(2, saveDokument.getGehoertZu());
			ins.setInt(3, saveDokument.getGehoertZuType());
			ins.setBinaryStream(4, is, (int) saveFile.length());
			int row = ins.executeUpdate();
			if (row > 0 ) {
				System.out.println("file wurde gespeichert");
			}
			ins.close();
		}
		

	}
}
