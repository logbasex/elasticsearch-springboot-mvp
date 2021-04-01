package logbasex.search.business.utils;

import logbasex.search.business.common.QueueConst;
import logbasex.search.business.factory.FactoryService;
import logbasex.search.db.dto.search.SearchItem;
import logbasex.search.db.enums.DatActionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.aws.messaging.config.annotation.NotificationMessage;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
@PropertySource("classpath:${spring.profiles.active:dev}_aws.properties")
public class SearchEngineSQSHandler {

	private final FactoryService factoryService;

	@Async(QueueConst.SEARCH_SQS_EXECUTOR)
	@SqsListener(value = "${elasticsearchSQSQueue}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
	public void updateSearchData(@NotificationMessage SearchItem message) {
		log.info("Handle message add data to search engine: " + message.toString());
		try {
			Integer action = message.getAction();
			if (DatActionEnum.CREATE.getId().equals(action)) {
				factoryService.getService(message).create(message);
			} else if (DatActionEnum.UPDATE.getId().equals(action)) {
				factoryService.getService(message).update(message);
			} else if (DatActionEnum.DELETE.getId().equals(action)) {
				factoryService.getService(message).delete(message);
			}
		} catch (Exception e) {
			log.error("Add data to search engine from queue error", e);
		}
	}
}
