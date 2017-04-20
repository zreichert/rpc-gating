def prepare(){
  common.conditionalStage(
    stage_name: "Prepare Deployment",
    stage: {
      if (env.STAGES.contains("Upgrade")) {
        common.prepareRpcGit(branch: env.UPGRADE_FROM_REF)
      } else {
        common.prepareRpcGit(branch: env.RPC_BRANCH)
      } // if
      dir("/opt/rpc-openstack"){
        withEnv( common.get_deploy_script_env() + [
          "DEPLOY_AIO=yes",
          "DEPLOY_OA=no",
          "DEPLOY_SWIFT=${env.DEPLOY_SWIFT}",
          "DEPLOY_ELK=${env.DEPLOY_ELK}",
          "DEPLOY_RPC=no"
        ]){
          sh """#!/bin/bash
          scripts/deploy.sh
          """
        } // env
      } // dir
      common.prepareConfigs(
        deployment_type: "aio"
      )
    } //stage param
  )
}
return this;