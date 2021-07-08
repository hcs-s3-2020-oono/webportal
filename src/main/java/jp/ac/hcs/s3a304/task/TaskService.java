package jp.ac.hcs.s3a304.task;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * タスク情報を操作する
 */
@Transactional
@Service
public class TaskService {
	@Autowired
	TaskRepository taskRepository;
	
	/**
	 * 指定したuser_idのタスクを全件取得する
	 * @param user_id
	 * @return TaskEntity
	 */
	public TaskEntity getTaskList(String user_id) {
		
		TaskEntity taskEntity;
		try {
			taskEntity = taskRepository.selectAll(user_id);
		}catch (DataAccessException e) {
			e.printStackTrace();
			taskEntity = null;
		}
		return taskEntity;
		
	}
	
	/**
	 * タスクを追加する
	 * @param user_id
	 * @param comment
	 * @param limitday
	 * @return 追加データ数
	 * @throws ParseException
	 */
	public boolean insertTask(String user_id, String title, String comment, String limitday, String priority) throws ParseException {
		
		boolean isSuccess = false;
		if(title == null || title.length() == 0 || title.length() > 50) {
			return isSuccess;
		} else if(comment == null || comment.length() == 0 || comment.length() > 50) {
			return isSuccess;
		} else if(!limitday.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")) {
			return isSuccess;
		}
		
		TaskData data = new TaskData();
		data.setUser_id(user_id);
		data.setPriority(Priority.id0f(Integer.parseInt(priority)));
		data.setTitle(title);
		data.setComment(comment);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date limitday2 = sdf.parse(limitday);
		data.setLimitday(limitday2);
		int rowNumber = taskRepository.insertOne(data);
		if(rowNumber == 1) {
			isSuccess=true;
		}
		return isSuccess;
		
	}

	public boolean deleteTask(int id) {
		boolean isSuccess = false;
		
		int rowNumber = taskRepository.deleteOne(id);
		if(rowNumber == 1) {
			isSuccess=true;
		}
		
		return isSuccess;
		
	}
	/**
	 * タスク情報をCSVファイルとしてサーバに保存する.
	 * @param user_id ユーザID
	 * @throws DataAccessException
	 */
	public void taskListCsvOut(String user_id) throws DataAccessException {
		taskRepository.tasklistCsvOut(user_id);
	}

	/**
	 * サーバーに保存されているファイルを取得して、byte配列に変換する.
	 * @param fileName ファイル名
	 * @return ファイルのbyte配列
	 * @throws IOException ファイル取得エラー
	 */
	public byte[] getFile(String fileName) throws IOException {
		FileSystem fs = FileSystems.getDefault();
		Path p = fs.getPath(fileName);
		byte[] bytes = Files.readAllBytes(p);
		return bytes;
	}

}
