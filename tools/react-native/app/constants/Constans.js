//服务器地址
//export const __CTX = 'http://www.hotent.org:9001/x5'
export let __CTX = "http://www.hotent.xyz:8083";
//export const MQTT_SERVER = 'www.hotent.org'
export const MQTT_SERVER = '192.168.1.127'
export const MQTT_PORT = 61614
//export const MQTT_URI = "mqtt://www.hotent.org:1883"

//工作台
export const WORK_TYPE_FLOW = '2';
export const WORK_TYPE_INNERWEB = '1';
export const WORK_TYPE_OTHERWEB = '0';
//消息
export const SESSION_P2P = 'p2p';
export const SESSION_TEAM = 'team';
export const MESSAGE_TYPE_TEXT = 'text';
export const MESSAGE_TYPE_IMAGE = 'image';
export const MESSAGE_TYPE_FILE = 'file';
export const MESSAGE_TYPE_AUDIO = 'audio';
export const MESSAGE_TYPE_VIDEO = 'video';
export const MESSAGE_TYPE_SYSTEM = 'system';


//消息状态
export const MESSAGE_STATUS_FIRST = 'first';
export const MESSAGE_STATUS_UPLOADING = 'uploading’';
export const MESSAGE_STATUS_UPLOADDONE = 'uploadDone’';
export const MESSAGE_STATUS_SENT = 'sent’';
export const MESSAGE_STATUS_UPLOADERROR = 'uploadError';


//语音文件的后缀名
export const MESSAGE_AUDIO_SUFFIX = 'aac';


//后台接收消息的Destination
export const MQTT_RECEIVE_DESTINATION = 'mqttReceiveDestination';

//流程状态
/**草稿*/
export const TASK_STATUS_DRAFT = 'draft';
/**运行中*/
export const TASK_STATUS_RUNNING = 'running';
/**结束*/
export const TASK_STATUS_END = 'end';
/**人工结束*/
export const TASK_STATUS_MANUAL_END = 'manualend';
/**驳回到发起人*/
export const TASK_STATUS_BACK_TOSTART = 'backToStart';
/**驳回*/
export const TASK_STATUS_BACK = 'back';
/**撤销*/
export const TASK_STATUS_REVOKE = 'revoke';
/**撤销到发起人*/
export const TASK_STATUS_REVOKE_TOSTART = 'revokeToStart';
