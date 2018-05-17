package com.csinfotechbd.document;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocServiceImpl implements DocService{
	
	@Autowired
	private AsyncFileService asyncService;

	@Override
	public <T> List<T> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getUniqueResult(T id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> boolean save(T object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> boolean update(T object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> boolean delete(T id) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public String saveFile(DocBearer bearer){
		MultipartFile file = bearer.getFile();
		String message = null;
		String generatedName = Generator.generateRandomIdWithSalt();
		String fileExt = FilenameUtils.getExtension(file.getOriginalFilename());
		// save image information only to database
		CompletableFuture<String> imageDb = asyncService.saveImageInfoToDb(bearer.getName(), generatedName,
				fileExt,file.getSize());
		@SuppressWarnings("unused")
		// save image file to system storage
		CompletableFuture<Void> imageStorage = asyncService.saveImageFileToStorage(file, generatedName, fileExt);
		try {
			// return in 2 second if the task fails in time
			return imageDb.get(2000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		} catch (ExecutionException ee) {
			ee.printStackTrace();
		} catch (TimeoutException te) {
			te.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}

}
