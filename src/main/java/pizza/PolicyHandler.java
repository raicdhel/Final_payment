package pizza;

import pizza.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{
    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @Autowired
    PaymentRepository PaymentRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrderCanceled_CancelPayment(@Payload OrderCanceled orderCanceled){

        if(orderCanceled.isMe()){
            Payment payment = new Payment();
            payment.setOrderId(orderCanceled.getId());
            payment.setPaymentStatus("Canceled");

            PaymentRepository.save(payment);

            System.out.println("##### listener CancelPayment : " + orderCanceled.toJson());

            // 서킷브레이크 테스트
            try {
                Thread.sleep((long) (400 + Math.random() * 300));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
