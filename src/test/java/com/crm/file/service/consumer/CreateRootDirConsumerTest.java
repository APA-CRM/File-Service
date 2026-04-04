package com.crm.file.service.consumer;

import com.crm.file.BaseIntegrationTestWithRabbitMQ;
import com.crm.file.persistance.repository.FileMetadataRepository;
import com.crm.sharedlib.messaging.dto.amqp.OrgCreatedMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.time.Duration;

import static com.crm.sharedlib.messaging.constants.RabbitMQConstants.CREATE_ROOT_DIR_QUEUE;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CreateRootDirConsumerTest extends BaseIntegrationTestWithRabbitMQ {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private FileMetadataRepository repository;

    @MockitoSpyBean
    private CreateRootDirConsumer createRootDirConsumer;

    @Test
    @DisplayName("Create root dir expected success")
    public void createRootDirExpectedSuccess() throws InterruptedException {

        OrgCreatedMessage message = new OrgCreatedMessage(1L, "Test organization");

        rabbitTemplate.convertAndSend(CREATE_ROOT_DIR_QUEUE, message);

        Thread.sleep(Duration.ofSeconds(2L));

        Mockito.verify(createRootDirConsumer, Mockito.atLeastOnce())
                .createRootDir(Mockito.any());

        assertFalse(repository.findAll().isEmpty());
    }

}