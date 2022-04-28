<template>
  <div class="wrapper">
    <div
        style="margin: 100px auto; background-color: #fff; width: 350px; height: 550px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>注 册</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input placeholder="请输入账号" size="medium" style="margin: 5px 0" prefix-icon="el-icon-user"
                    v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input placeholder="请输入密码" size="medium" style="margin: 5px 0" prefix-icon="el-icon-lock" show-password
                    v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input placeholder="请确认密码" size="medium" style="margin: 5px 0" prefix-icon="el-icon-lock" show-password
                    v-model="user.confirmPassword"></el-input>
        </el-form-item>

        <el-form-item label="头像:" prop="avatarUrl">
          <el-upload class="avatar-uploader showUploader"
                     ref="uploadFile"
                     :auto-upload="false"
                     :on-preview="handlePreview"
                     :before-remove="beforeRemove"
                     :on-change="uploadSectionFile"
                     :limit="2" action="" accept=".jpg,.png,.jpeg">
            <img v-if="photo" :src="photo" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>

        </el-form-item>
        <el-form-item style="margin: 5px 0; text-align: center">
          <el-button type="primary" size="small" autocomplete="off" @click="login">注册</el-button>
          <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/login')">返回登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import {serverIp} from "../../public/config";

export default {
  name: "Login",
  data() {
    return {
      user: {},
      serverIp: serverIp,
      files: {},
      photo: '',
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 3, max: 10, message: '长度在 3 到 5 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
        confirmPassword: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
      }
    }
  },
  methods: {
    login() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          if (this.user.password !== this.user.confirmPassword) {
            this.$message.error("两次输入的密码不一致")
            return false
          }
          this.request.post("/user/register", this.user).then(res => {
            if (res.code === '200') {
              this.$message.success("注册成功")
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      });
    },
    handlePreview(file) {
      console.log(file);
    },
    beforeRemove(file) {
      return this.$confirm(`确定移除 ${ file.name }？`);
    },
    uploadSectionFile(file){
      console.log('this.$refs.uploadFile:'+this.$refs.uploadFile.uploadFiles)
      // 解决上传文件limit为1时，不触发onchange方法，在第二次调用的时候将第一个文件删除
      if(this.$refs.uploadFile.uploadFiles.length>1){
        this.$refs.uploadFile.uploadFiles.shift();
      }
      console.log('uploadSectionFile.....')
      const typeArr = ['image/png', 'image/jpeg', 'image/jpg'];
      const isJPG = typeArr.indexOf(file.raw.type) !== -1;
      const isLt3M = file.size / 1024 / 1024 < 3;
      if (!isJPG) {
        this.$message.error('只能是图片png jpg jpeg!');
        this.files = null;
        return;
      }
      if (!isLt3M) {
        this.$message.error('上传图片大小不能超过 3MB!');
        this.files = null;
        return;
      }
      this.files = file.raw;
      // FormData 对象
      var formData = new FormData();
      // 文件对象
      formData.append('file', this.files);
      let config = {
        headers: {
          'Content-Type': 'multipart/form-data'
        },
        methods: 'post'
      }
      this.request.post("/file/common/upload",formData,config).then(res=>{
        console.log('头像上传结果:'+res)
        if(res){
          this.photo = res
          this.user.avatarUrl=res
        }else{
          this.$message.error('上传失败')
        }
      })
    },
  }
}
</script>

<style>
.wrapper {
  height: 100vh;
  background-image: linear-gradient(to bottom right, #FC466B, #3F5EFB);
  overflow: hidden;
}
</style>
