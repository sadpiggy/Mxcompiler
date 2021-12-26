.text
	.file	"builtin.c"
	.globl	print                   # -- Begin function print
	.p2align	2
	.type	print,@function
print:                                  # @print
	.cfi_startproc
# %bb.0:                                # %entry
	lui	a1, %hi(.L.str)
	addi	a1, a1, %lo(.L.str)
	mv	a2, a0
	mv	a0, a1
	mv	a1, a2
	tail	printf
.Lfunc_end0:
	.size	print, .Lfunc_end0-print
	.cfi_endproc
                                        # -- End function
	.globl	println                 # -- Begin function println
	.p2align	2
	.type	println,@function
println:                                # @println
	.cfi_startproc
# %bb.0:                                # %entry
	tail	puts
.Lfunc_end1:
	.size	println, .Lfunc_end1-println
	.cfi_endproc
                                        # -- End function
	.globl	printInt                # -- Begin function printInt
	.p2align	2
	.type	printInt,@function
printInt:                               # @printInt
	.cfi_startproc
# %bb.0:                                # %entry
	lui	a1, %hi(.L.str.2)
	addi	a1, a1, %lo(.L.str.2)
	mv	a2, a0
	mv	a0, a1
	mv	a1, a2
	tail	printf
.Lfunc_end2:
	.size	printInt, .Lfunc_end2-printInt
	.cfi_endproc
                                        # -- End function
	.globl	printlnInt              # -- Begin function printlnInt
	.p2align	2
	.type	printlnInt,@function
printlnInt:                             # @printlnInt
	.cfi_startproc
# %bb.0:                                # %entry
	lui	a1, %hi(.L.str.3)
	addi	a1, a1, %lo(.L.str.3)
	mv	a2, a0
	mv	a0, a1
	mv	a1, a2
	tail	printf
.Lfunc_end3:
	.size	printlnInt, .Lfunc_end3-printlnInt
	.cfi_endproc
                                        # -- End function
	.globl	_malloc                 # -- Begin function _malloc
	.p2align	2
	.type	_malloc,@function
_malloc:                                # @_malloc
	.cfi_startproc
# %bb.0:                                # %entry
	srai	a1, a0, 31
	tail	malloc
.Lfunc_end4:
	.size	_malloc, .Lfunc_end4-_malloc
	.cfi_endproc
                                        # -- End function
	.globl	getString               # -- Begin function getString
	.p2align	2
	.type	getString,@function
getString:                              # @getString
	.cfi_startproc
# %bb.0:                                # %entry
	addi	sp, sp, -16
	.cfi_def_cfa_offset 16
	sw	ra, 12(sp)
	sw	s0, 8(sp)
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	addi	a0, zero, 1024
	mv	a1, zero
	call	malloc
	mv	s0, a0
	lui	a0, %hi(.L.str)
	addi	a0, a0, %lo(.L.str)
	mv	a1, s0
	call	__isoc99_scanf
	mv	a0, s0
	lw	s0, 8(sp)
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end5:
	.size	getString, .Lfunc_end5-getString
	.cfi_endproc
                                        # -- End function
	.globl	getInt                  # -- Begin function getInt
	.p2align	2
	.type	getInt,@function
getInt:                                 # @getInt
	.cfi_startproc
# %bb.0:                                # %entry
	addi	sp, sp, -16
	.cfi_def_cfa_offset 16
	sw	ra, 12(sp)
	.cfi_offset ra, -4
	lui	a0, %hi(.L.str.2)
	addi	a0, a0, %lo(.L.str.2)
	addi	a1, sp, 8
	call	__isoc99_scanf
	lw	a0, 8(sp)
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end6:
	.size	getInt, .Lfunc_end6-getInt
	.cfi_endproc
                                        # -- End function
	.globl	toString                # -- Begin function toString
	.p2align	2
	.type	toString,@function
toString:                               # @toString
	.cfi_startproc
# %bb.0:                                # %entry
	addi	sp, sp, -16
	.cfi_def_cfa_offset 16
	sw	ra, 12(sp)
	sw	s0, 8(sp)
	sw	s1, 4(sp)
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	.cfi_offset s1, -12
	mv	s0, a0
	addi	a0, zero, 15
	mv	a1, zero
	call	malloc
	mv	s1, a0
	lui	a0, %hi(.L.str.2)
	addi	a1, a0, %lo(.L.str.2)
	mv	a0, s1
	mv	a2, s0
	call	sprintf
	mv	a0, s1
	lw	s1, 4(sp)
	lw	s0, 8(sp)
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end7:
	.size	toString, .Lfunc_end7-toString
	.cfi_endproc
                                        # -- End function
	.globl	builtin.string.add      # -- Begin function builtin.string.add
	.p2align	2
	.type	builtin.string.add,@function
builtin.string.add:                     # @builtin.string.add
	.cfi_startproc
# %bb.0:                                # %entry
	addi	sp, sp, -16
	.cfi_def_cfa_offset 16
	sw	ra, 12(sp)
	sw	s0, 8(sp)
	sw	s1, 4(sp)
	sw	s2, 0(sp)
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	.cfi_offset s1, -12
	.cfi_offset s2, -16
	mv	s2, a1
	mv	s1, a0
	addi	a0, zero, 1024
	mv	a1, zero
	call	malloc
	mv	s0, a0
	mv	a1, s1
	call	strcpy
	mv	a0, s0
	mv	a1, s2
	lw	s2, 0(sp)
	lw	s1, 4(sp)
	lw	s0, 8(sp)
	lw	ra, 12(sp)
	addi	sp, sp, 16
	tail	strcat
.Lfunc_end8:
	.size	builtin.string.add, .Lfunc_end8-builtin.string.add
	.cfi_endproc
                                        # -- End function
	.globl	builtin.string.isEqual  # -- Begin function builtin.string.isEqual
	.p2align	2
	.type	builtin.string.isEqual,@function
builtin.string.isEqual:                 # @builtin.string.isEqual
	.cfi_startproc
# %bb.0:                                # %entry
	addi	sp, sp, -16
	.cfi_def_cfa_offset 16
	sw	ra, 12(sp)
	.cfi_offset ra, -4
	call	strcmp
	seqz	a0, a0
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end9:
	.size	builtin.string.isEqual, .Lfunc_end9-builtin.string.isEqual
	.cfi_endproc
                                        # -- End function
	.globl	builtin.string.isNotEqual # -- Begin function builtin.string.isNotEqual
	.p2align	2
	.type	builtin.string.isNotEqual,@function
builtin.string.isNotEqual:              # @builtin.string.isNotEqual
	.cfi_startproc
# %bb.0:                                # %entry
	addi	sp, sp, -16
	.cfi_def_cfa_offset 16
	sw	ra, 12(sp)
	.cfi_offset ra, -4
	call	strcmp
	snez	a0, a0
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end10:
	.size	builtin.string.isNotEqual, .Lfunc_end10-builtin.string.isNotEqual
	.cfi_endproc
                                        # -- End function
	.globl	builtin.string.isLessThan # -- Begin function builtin.string.isLessThan
	.p2align	2
	.type	builtin.string.isLessThan,@function
builtin.string.isLessThan:              # @builtin.string.isLessThan
	.cfi_startproc
# %bb.0:                                # %entry
	addi	sp, sp, -16
	.cfi_def_cfa_offset 16
	sw	ra, 12(sp)
	.cfi_offset ra, -4
	call	strcmp
	srli	a0, a0, 31
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end11:
	.size	builtin.string.isLessThan, .Lfunc_end11-builtin.string.isLessThan
	.cfi_endproc
                                        # -- End function
	.globl	builtin.string.isGreaterThan # -- Begin function builtin.string.isGreaterThan
	.p2align	2
	.type	builtin.string.isGreaterThan,@function
builtin.string.isGreaterThan:           # @builtin.string.isGreaterThan
	.cfi_startproc
# %bb.0:                                # %entry
	addi	sp, sp, -16
	.cfi_def_cfa_offset 16
	sw	ra, 12(sp)
	.cfi_offset ra, -4
	call	strcmp
	sgtz	a0, a0
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end12:
	.size	builtin.string.isGreaterThan, .Lfunc_end12-builtin.string.isGreaterThan
	.cfi_endproc
                                        # -- End function
	.globl	builtin.string.isLessThanOrEqual # -- Begin function builtin.string.isLessThanOrEqual
	.p2align	2
	.type	builtin.string.isLessThanOrEqual,@function
builtin.string.isLessThanOrEqual:       # @builtin.string.isLessThanOrEqual
	.cfi_startproc
# %bb.0:                                # %entry
	addi	sp, sp, -16
	.cfi_def_cfa_offset 16
	sw	ra, 12(sp)
	.cfi_offset ra, -4
	call	strcmp
	slti	a0, a0, 1
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end13:
	.size	builtin.string.isLessThanOrEqual, .Lfunc_end13-builtin.string.isLessThanOrEqual
	.cfi_endproc
                                        # -- End function
	.globl	builtin.string.isGreaterThanOrEqual # -- Begin function builtin.string.isGreaterThanOrEqual
	.p2align	2
	.type	builtin.string.isGreaterThanOrEqual,@function
builtin.string.isGreaterThanOrEqual:    # @builtin.string.isGreaterThanOrEqual
	.cfi_startproc
# %bb.0:                                # %entry
	addi	sp, sp, -16
	.cfi_def_cfa_offset 16
	sw	ra, 12(sp)
	.cfi_offset ra, -4
	call	strcmp
	not	a0, a0
	srli	a0, a0, 31
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end14:
	.size	builtin.string.isGreaterThanOrEqual, .Lfunc_end14-builtin.string.isGreaterThanOrEqual
	.cfi_endproc
                                        # -- End function
	.globl	builtin.string.length   # -- Begin function builtin.string.length
	.p2align	2
	.type	builtin.string.length,@function
builtin.string.length:                  # @builtin.string.length
	.cfi_startproc
# %bb.0:                                # %entry
	addi	sp, sp, -16
	.cfi_def_cfa_offset 16
	sw	ra, 12(sp)
	.cfi_offset ra, -4
	call	strlen
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end15:
	.size	builtin.string.length, .Lfunc_end15-builtin.string.length
	.cfi_endproc
                                        # -- End function
	.globl	builtin.string.substring # -- Begin function builtin.string.substring
	.p2align	2
	.type	builtin.string.substring,@function
builtin.string.substring:               # @builtin.string.substring
	.cfi_startproc
# %bb.0:                                # %entry
	addi	sp, sp, -32
	.cfi_def_cfa_offset 32
	sw	ra, 28(sp)
	sw	s0, 24(sp)
	sw	s1, 20(sp)
	sw	s2, 16(sp)
	sw	s3, 12(sp)
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	.cfi_offset s1, -12
	.cfi_offset s2, -16
	.cfi_offset s3, -20
	mv	s3, a1
	mv	s2, a0
	sub	s1, a2, a1
	addi	a0, s1, 1
	srai	a1, a0, 31
	call	malloc
	mv	s0, a0
	add	a1, s2, s3
	mv	a2, s1
	call	memcpy
	add	a0, s0, s1
	sb	zero, 0(a0)
	mv	a0, s0
	lw	s3, 12(sp)
	lw	s2, 16(sp)
	lw	s1, 20(sp)
	lw	s0, 24(sp)
	lw	ra, 28(sp)
	addi	sp, sp, 32
	ret
.Lfunc_end16:
	.size	builtin.string.substring, .Lfunc_end16-builtin.string.substring
	.cfi_endproc
                                        # -- End function
	.globl	builtin.string.parseInt # -- Begin function builtin.string.parseInt
	.p2align	2
	.type	builtin.string.parseInt,@function
builtin.string.parseInt:                # @builtin.string.parseInt
	.cfi_startproc
# %bb.0:                                # %entry
	addi	sp, sp, -16
	.cfi_def_cfa_offset 16
	sw	ra, 12(sp)
	.cfi_offset ra, -4
	lui	a1, %hi(.L.str.2)
	addi	a1, a1, %lo(.L.str.2)
	addi	a2, sp, 8
	call	__isoc99_sscanf
	lw	a0, 8(sp)
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end17:
	.size	builtin.string.parseInt, .Lfunc_end17-builtin.string.parseInt
	.cfi_endproc
                                        # -- End function
	.globl	builtin.string.ord      # -- Begin function builtin.string.ord
	.p2align	2
	.type	builtin.string.ord,@function
builtin.string.ord:                     # @builtin.string.ord
	.cfi_startproc
# %bb.0:                                # %entry
	add	a0, a0, a1
	lb	a0, 0(a0)
	ret
.Lfunc_end18:
	.size	builtin.string.ord, .Lfunc_end18-builtin.string.ord
	.cfi_endproc
                                        # -- End function
	.type	.L.str,@object          # @.str
	.section	.rodata.str1.1,"aMS",@progbits,1
.L.str:
	.asciz	"%s"
	.size	.L.str, 3

	.type	.L.str.2,@object        # @.str.2
.L.str.2:
	.asciz	"%d"
	.size	.L.str.2, 3

	.type	.L.str.3,@object        # @.str.3
.L.str.3:
	.asciz	"%d\n"
	.size	.L.str.3, 4

	.ident	"clang version 10.0.0-4ubuntu1~18.04.2 "
	.section	".note.GNU-stack","",@progbits