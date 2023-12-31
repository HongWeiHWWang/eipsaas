package com.hotent.uc.util;

import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class UserPhotoFileFindVisitor extends SimpleFileVisitor<Path>{
	private List<String> filenameList = new ArrayList<String>();
	// 例外文件
	private String[] exceptFiles  = null;

	public UserPhotoFileFindVisitor(String...exceptFiles) {
		this.exceptFiles = exceptFiles;
	}

	private Boolean isSame(String excptFile, Path file){
		String fileName = file.getFileName().toString();
		try {
			Pattern regex = Pattern.compile("^\\w+/(\\d+\\.\\w+)$");
			Matcher regexMatcher = regex.matcher(excptFile);
			if (regexMatcher.matches()) {
				String result = regexMatcher.group(1);
				return result.equals(fileName);
			} else {
				return false;
			} 
		} catch (PatternSyntaxException ex) {
			return false;
		}
	}

	@Override  
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
		if(exceptFiles!=null && exceptFiles.length > 0){
			for(String excptFile : exceptFiles){
				if(isSame(excptFile, file)){
					return FileVisitResult.CONTINUE;
				}
			}
		}
		filenameList.add(file.toString());
		return FileVisitResult.CONTINUE;
	}  

	public List<String> getFilenameList() {  
		return filenameList;  
	}  

	public void setFilenameList(List<String> filenameList) {  
		this.filenameList = filenameList;  
	}  
}
