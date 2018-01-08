def deploy(){
  common.conditionalStep(
    step_name: "Deploy",
    step: {
      List playbooks = common._parse_json_string(json_text: env.PLAYBOOKS)
      print(playbooks)
      print(playbooks.size())
      for (def i=0; i<playbooks.size(); i++){
        String playbook = playbooks[i]
        print(playbook)
        stage(playbook.playbook){
          common.openstack_ansible(playbook)
        } //stage
      } // for
    } //  step
  ) //conditionalStep
}

def deploy_sh(Map args) {
  common.conditionalStage(
    stage_name: "Deploy RPC w/ Script",
    stage: {
      List environment_vars = args.environment_vars \
                              + common.get_deploy_script_env()
      withCredentials([
        string(
          credentialsId: "INFLUX_IP",
          variable: "INFLUX_IP"
        )
      ]){
        withEnv(environment_vars) {
          dir("/opt/rpc-openstack/") {
            sh """#!/bin/bash
            scripts/deploy.sh
            """
          } // dir
        } // withEnv
      } //withCredentials
    } // stage
  ) // conditionalStage
}

return this;
