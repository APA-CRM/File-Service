package com.crm.file.service.consumer;

import com.crm.file.persistance.entity.FileMetadata;
import com.crm.file.service.FileService;
import com.crm.sharedlib.messaging.dto.amqp.OrgCreatedMessage;
import com.crm.sharedlib.messaging.dto.amqp.OrgRootDirCreatedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.crm.sharedlib.messaging.constants.RabbitMQConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateRootDirConsumer {

    private final FileService fileService;

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.files.default-root-dir-prefix-name}")
    private String rootDirPrefixName;

    @RabbitListener(queues = CREATE_ROOT_DIR_QUEUE)
    public void createRootDir(OrgCreatedMessage message) {
        log.debug("Creating root dir for organization {}", message.getOrganizationId());

        try {
            FileMetadata file = fileService.createDefaultDirectory(rootDirPrefixName + " " + message.getOrganizationName());

            OrgRootDirCreatedMessage rootDirCreatedMessage =
                    new OrgRootDirCreatedMessage(message.getOrganizationId(), file.getId());

            rabbitTemplate.convertAndSend(FILE_SERVICE_EXCHANGE_NAME, ORGANIZATION_ROOT_DIR_CREATE_ROUTING_KEY, rootDirCreatedMessage);

            log.debug("Root dir has been created for organization {}. Reply event has been sent", message.getOrganizationId());
        } catch (Exception e) {
            log.error("Error has occurred while creating root dir for organization {}", message.getOrganizationId(), e);
            throw e;
        }
    }

}
