package jp.ac.hcs.s3a304.task;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.ac.hcs.s3a304.WebConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class TaskController {

	@Autowired
	private TaskService taskService;

	/**
	 * タスク管理画面を表示する
	 * @param principal ログイン情報
	 * @param model
	 * @return 結果画面
	 */
	@PostMapping("/task")
	public String getTask(Principal principal, Model model) {
		
		TaskEntity taskEntity = taskService.getTaskList(principal.getName());
		if (taskEntity == null) {
			model.addAttribute("errorMSG", "タスクがNullです");
		} else {
			model.addAttribute("taskEntity", taskEntity);
		}
			return "task/task";
	}
	
	@PostMapping("/task/insert")
	public String insertTask(@RequestParam(name = "comment", required = false) String comment,
			                 @RequestParam(name = "limitday", required = false) String limitday,
			                 @RequestParam(name = "priority", required = false) String priority,
			                 @RequestParam(name = "title", required = false) String title,
			                 Principal principal, Model model) throws ParseException {
		
		boolean isSuccess = taskService.insertTask(principal.getName(), title, comment, limitday, priority);
		log.info("["+principal.getName()+"]"+"タスク管理追加："+ title + "," + comment + "," + limitday + "," + priority);
		if(isSuccess) {
			model.addAttribute("okMSG", "正常に追加されました");
		}else {
			model.addAttribute("errorMSG", "入力値に誤りがあります");
		}
		return getTask(principal, model);
	}
	
	@RequestMapping("/task/delete/{id}")
	public String deleteTask(@PathVariable int id, Principal principal, Model model) {
		
		boolean isSuccess = taskService.deleteTask(id);
		log.info("["+principal.getName()+"]"+"タスク管理削除："+ id);
		if(isSuccess) {
			model.addAttribute("okMSG", "正常に削除されました");
		}else {
			model.addAttribute("errorMSG", "削除できませんでした。再度手順をやり直してください");
		}
		return getTask(principal, model);
	}
	
	/**
	 * 自分の全てのタスク情報をCSVファイルとしてダウンロードさせる.
	 * @param principal ログイン情報
	 * @param model
	 * @return タスク情報のCSVファイル
	 */
	@PostMapping("/task/csv")
	public ResponseEntity<byte[]> getTaskCsv(Principal principal, Model model) {

		final String OUTPUT_FULLPATH = WebConfig.OUTPUT_PATH + WebConfig.FILENAME_TASK_CSV;

		log.info("[" + principal.getName() + "]CSVファイル作成:" + OUTPUT_FULLPATH);

		// CSVファイルをサーバ上に作成
		taskService.taskListCsvOut(principal.getName());

		// CSVファイルをサーバから読み込み
		byte[] bytes = null;
		try {
			bytes = taskService.getFile(OUTPUT_FULLPATH);
			log.info("[" + principal.getName() + "]CSVファイル読み込み成功:" + OUTPUT_FULLPATH);
		} catch (IOException e) {
			log.warn("[" + principal.getName() + "]CSVファイル読み込み失敗:" + OUTPUT_FULLPATH);
			e.printStackTrace();
		}

		// CSVファイルのダウンロード用ヘッダー情報設定
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "text/csv; charset=UTF-8");
		header.setContentDispositionFormData("filename", WebConfig.FILENAME_TASK_CSV);

		// CSVファイルを端末へ送信
		return new ResponseEntity<byte[]>(bytes, header, HttpStatus.OK);
	}
	
}
