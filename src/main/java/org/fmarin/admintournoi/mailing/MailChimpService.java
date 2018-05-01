package org.fmarin.admintournoi.mailing;

import com.ecwid.maleorang.MailchimpClient;
import com.ecwid.maleorang.MailchimpException;
import com.ecwid.maleorang.MailchimpObject;
import com.ecwid.maleorang.method.v3_0.campaigns.GetCampaignsMethod;
import com.ecwid.maleorang.method.v3_0.lists.members.EditMemberMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MailChimpService {

  private static final Logger logger = LoggerFactory.getLogger(MailChimpService.class);

  private final MailChimpProperties properties;

  @Autowired
  public MailChimpService(MailChimpProperties properties) {
    this.properties = properties;
  }

  public void subscribe(String email, String teamName, Integer verificationCode) {
    MailchimpClient client = new MailchimpClient(properties.getApiKey());
    try {
      EditMemberMethod.CreateOrUpdate method = new EditMemberMethod.CreateOrUpdate(properties.getListId(), email);
      method.status = "subscribed";
      method.merge_fields = new MailchimpObject();
      method.merge_fields.mapping.put("TEAM_NAME", teamName);
      method.merge_fields.mapping.put("VERIF_CODE", verificationCode);

      client.execute(method);
      logger.info("Team {} with email {} has been successfully subscribed", teamName, email);
    } catch (Exception e) {
      logger.error("MailChimp subscription failed for team {} with email {}", teamName, email, e);
    } finally {
      try {
        client.close();
      } catch (IOException e) {
        logger.error("MailChimp client failed", e);
      }
    }
  }

  public GetCampaignsMethod.Response getCampaignStats() {
    MailchimpClient client = new MailchimpClient(properties.getApiKey());
    GetCampaignsMethod method = new GetCampaignsMethod();
    try {
      GetCampaignsMethod.Response execute = client.execute(method);
      return execute;
    } catch (IOException | MailchimpException e) {
      e.printStackTrace();
    }
    return null;
  }
}
