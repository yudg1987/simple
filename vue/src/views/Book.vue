<template>
  <div class="block" style="width: 100%;height: 100%">
    <el-table
        :data="books"
        border
        style="width: 100%">
      <el-table-column
          fixed
          prop="id"
          label="编号"
          width="100PX">
      </el-table-column>
      <el-table-column
          prop="name"
          label="书名"
          width="300PX">
      </el-table-column>
      <el-table-column
          prop="author"
          label="作者"
          width="300PX">
      </el-table-column>
      <el-table-column
          fixed="right"
          label="操作"
          width="300PX">
        <template slot-scope="scope">
          <el-button @click="handleClick(scope.row)" type="text" size="small">修改</el-button>
          <el-button type="text" size="small" @click="deleteRow(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page.sync="currentPage"
        :page-sizes="[10, 20, 50, 100]"
        :page-size.sync="currentPageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>

    <el-dialog title="书本信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="120px" size="small" :model="bookForm" :rules="rules" ref="bookForm">
        <el-form-item label="书名" prop="name">
          <el-input v-model="bookForm.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="作者" prop="author">
          <el-input v-model="bookForm.author" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="id" prop="id" hidden>
          <el-input v-model="bookForm.id" autocomplete="off" hidden></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="dialogFormVisible = false">取 消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>

export default {
  name: "Book",
  data() {
    return {
      books: [],
      currentPage: 1,
      currentPageSize: 10,
      total: 0,
      dialogFormVisible: false,
      bookForm: {
        id: 0,
        name: '',
        author: ''
      },
      rules: {
        name: [
          {required: true, message: '请输入书籍名称', trigger: 'blur'},
          {min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur'}
        ],
        author: [
          {required: true, message: '请输入作者', trigger: 'blur'},
          {min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur'}
        ]
      }
    }
  },
  mounted() {
    this.doQuery()
  },
  methods: {
    handleClick(row) {
      console.log(row)
      this.bookForm = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    submitForm() {

      this.$refs.bookForm.validate((valid) => {
        console.info('submitForm:' + valid + ',bookForm' + JSON.stringify(this.bookForm))
        if (valid) {
          // alert('submit!')
          this.request.post('/book/updateBook/', this.bookForm).then(resp => {
            console.log('submitForm:' + resp)
            if (resp.code === '200') {
              this.dialogFormVisible = false
              this.doQuery()
            }
          }).catch(error => {
            console.log(error)
            this.$message.error('更新失败')
          })
        }
      })
    },
    deleteRow(row) {
      console.log("删除记录:" + row.id)
      this.$confirm("确认删除吗", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(async () => {
        this.request.delete("/book/delete/" + row.id).then(resp => {
          console.log(resp)
          if (resp.code === '200') {
            this.$message.success("删除记录成功")
            // 动态刷新页面
            window.location.reload()
          }
        }).catch(error => {
          console.log(error)
          this.$message.error('删除失败')
        })
      }).catch(() => {
        this.$message({
          type: "error",
          message: "取消删除!"
        });
      })
    },
    page(currentPpage) {
      this.currentPage = currentPpage
      console.log("currentPpage:" + currentPpage)
      this.doQuery()
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`)
      this.currentPageSize = val
      this.doQuery()
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`)
      this.currentPage = val
      this.doQuery()
    },
    doQuery() {
      this.request.get('/book/getBooks/' + this.currentPage + "/" + this.currentPageSize).then(res => {
        if (res.code === '200') {
          console.log(res)
          this.books = res.data.records
          this.total = res.data.total
        } else {
          this.$message.success(res.msg)
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
