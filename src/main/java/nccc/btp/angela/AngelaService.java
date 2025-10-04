//package nccc.btp.angela;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.client.RestTemplateBuilder;
//
//public class AngelaService {
//  private static final int mode = 3001;
//  private static final String dataFormat = "hex";
//  private static final String responseFormat = "hex";
//
//  public String encrypt(String text) {
//    log.debug("encrypt {}, text:{}", angelaConfig, text);
//    RestTemplate restTemplate =
//        restTemplateBuilder.setConnectTimeout(Duration.ofSeconds(angelaConfig.getConnectTimeout()))
//            .setReadTimeout(Duration.ofSeconds(angelaConfig.getReadTimeout()))
//            .requestFactory(HttpsClientRequestFactory.class).build();
//    RequestVo req =
//        RequestVo.builder().ssoid(angelaConfig.getSsoId()).keyName(angelaConfig.getLable())
//            .encryptMode(mode).dataFormat(dataFormat).responseFormat(responseFormat)
//            .data(Hex.encodeHexString(text.getBytes(StandardCharsets.UTF_8))).build();
//    ResponseVo resp =
//        restTemplate.postForObject(angelaConfig.getUrl() + "/v1/encrypt", req, ResponseVo.class);
//    log.debug("encrypt result {}", resp);
//    return resp.getData();
//  }
//
//  public String decrypt(String hex) throws DecoderException {
//    log.debug("decrypt {}, hex:{}", angelaConfig, hex);
//    RestTemplate restTemplate =
//        restTemplateBuilder.setConnectTimeout(Duration.ofSeconds(angelaConfig.getConnectTimeout()))
//            .setReadTimeout(Duration.ofSeconds(angelaConfig.getReadTimeout()))
//            .requestFactory(HttpsClientRequestFactory.class).build();
//    RequestVo req = RequestVo.builder().ssoid(angelaConfig.getSsoId())
//        .keyName(angelaConfig.getLable()).decryptMode(mode).dataFormat(dataFormat)
//        .responseFormat(responseFormat).data(hex).build();
//    ResponseVo resp =
//        restTemplate.postForObject(angelaConfig.getUrl() + "/v1/decrypt", req, ResponseVo.class);
//    log.debug("decrypt result {}", resp);
//    if (resp.getData() != null) {
//      byte[] data = Hex.decodeHex(resp.getData());
//      String result = new String(data, StandardCharsets.UTF_8);
//      return result;
//    } else {
//      return null;
//    }
//  }
//
//  @Autowired
//  private AngelaConfig angelaConfig;
//
//  @Autowired
//  private RestTemplateBuilder restTemplateBuilder;
//}
