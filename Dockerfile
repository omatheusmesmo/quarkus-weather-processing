FROM rabbitmq:4.1

EXPOSE 5672 5672

# Enable RabbitMQ management plugin (for the web UI)
RUN rabbitmq-plugins enable --offline rabbitmq_management
